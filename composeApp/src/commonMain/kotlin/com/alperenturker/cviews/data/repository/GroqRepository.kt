package com.alperenturker.cviews.data.repository

import com.alperenturker.cviews.config.getGroqApiKey
import com.alperenturker.cviews.currentTimeMillis
import com.alperenturker.cviews.data.network.GroqChatRequest
import com.alperenturker.cviews.data.network.GroqChatResponse
import com.alperenturker.cviews.data.network.GroqMessage
import com.alperenturker.cviews.data.network.createHttpClient
import com.alperenturker.cviews.domain.model.AtsScore
import com.alperenturker.cviews.domain.model.CvAnalysis
import com.alperenturker.cviews.domain.model.ExtractedProfile
import com.alperenturker.cviews.domain.model.ImprovementSuggestion
import com.alperenturker.cviews.domain.model.StrengthWeaknessItem
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

private const val GROQ_URL = "https://api.groq.com/openai/v1/chat/completions"

class GroqRepository {

    private val client = createHttpClient()
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun analyzeCv(
        cvText: String,
        fileName: String,
        targetRole: String,
    ): Result<CvAnalysis> = withContext(Dispatchers.Default) {
        val apiKey = getGroqApiKey()
        if (apiKey.isBlank()) {
            return@withContext Result.failure(IllegalStateException("GROQ_API_KEY not set. Add to local.properties (Android) or env (other)."))
        }
        val systemPrompt = """
Sen bir CV/özgeçmiş analiz uzmanısın. Verilen CV metnini analiz et ve ATS (Applicant Tracking System) uyumluluğu, profil, güçlü/zayıf yönler, iyileştirme önerileri ve eksik anahtar kelimeler çıkar.
Kullanıcı hedef rolü zorunlu olarak belirtmiştir: "$targetRole". Analizi özellikle bu role göre yap; ATS skoru ve önerileri bu hedef role göre değerlendir.
Yanıtını SADECE aşağıdaki JSON formatında ver, başka metin ekleme. Sayılar 0-100 arası tam sayı olmalı.

{
  "atsScore": { "total": 0, "parsingScore": 0, "keywordMatch": 0, "structureScore": 0, "readabilityScore": 0, "impactScore": 0 },
  "profile": { "name": "", "detectedRole": "", "yearsOfExperience": 0, "topSkills": [], "education": "" },
  "strengths": [ { "title": "", "explanation": "" } ],
  "weaknesses": [ { "title": "", "explanation": "" } ],
  "improvementSuggestions": [ { "title": "", "explanation": "" } ],
  "missingKeywords": []
}
""".trimIndent()
        val userContent = buildString {
            append("CV metni:\n\n")
            append(cvText.take(12000))
            append("\n\nHedef rol (kullanıcının belirttiği): $targetRole")
        }
        val request = GroqChatRequest(
            model = "llama-3.3-70b-versatile",
            messages = listOf(
                GroqMessage(role = "system", content = systemPrompt),
                GroqMessage(role = "user", content = userContent),
            ),
        )
        try {
            val response: GroqChatResponse = client.post(GROQ_URL) {
                header("Authorization", "Bearer $apiKey")
                contentType(ContentType.Application.Json)
                setBody(request)
            }.body()
            val error = response.error
            if (error != null) {
                return@withContext Result.failure(RuntimeException(error.message ?: "Groq API error"))
            }
            val content = response.choices?.firstOrNull()?.message?.content
                ?: return@withContext Result.failure(RuntimeException("Boş yanıt"))
            val analysis = parseContentToAnalysis(content, fileName, targetRole)
            Result.success(analysis)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun parseContentToAnalysis(content: String, fileName: String, targetRole: String): CvAnalysis {
        val raw = content.trim().removePrefix("```json").removePrefix("```").removeSuffix("```").trim()
        val obj = json.parseToJsonElement(raw).jsonObject
        val ats = obj["atsScore"]?.jsonObject ?: return fallbackAnalysis(fileName, targetRole)
        val total = ats["total"]?.jsonPrimitive?.content?.toIntOrNull() ?: 70
        val atsScore = AtsScore(
            total = total,
            parsingScore = ats["parsingScore"]?.jsonPrimitive?.content?.toIntOrNull() ?: total,
            keywordMatch = ats["keywordMatch"]?.jsonPrimitive?.content?.toIntOrNull() ?: total,
            structureScore = ats["structureScore"]?.jsonPrimitive?.content?.toIntOrNull() ?: total,
            readabilityScore = ats["readabilityScore"]?.jsonPrimitive?.content?.toIntOrNull() ?: total,
            impactScore = ats["impactScore"]?.jsonPrimitive?.content?.toIntOrNull() ?: total,
        )
        val profileObj = obj["profile"]?.jsonObject
        val profile = ExtractedProfile(
            name = profileObj?.get("name")?.jsonPrimitive?.content ?: "—",
            detectedRole = profileObj?.get("detectedRole")?.jsonPrimitive?.content?.takeIf { it.isNotBlank() },
            yearsOfExperience = profileObj?.get("yearsOfExperience")?.jsonPrimitive?.content?.toIntOrNull(),
            topSkills = profileObj?.get("topSkills")?.jsonArray?.mapNotNull { it.jsonPrimitive.content }.orEmpty(),
            education = profileObj?.get("education")?.jsonPrimitive?.content?.takeIf { it.isNotBlank() },
        )
        val strengths = (obj["strengths"]?.jsonArray ?: emptyList()).map {
            val o = it.jsonObject
            StrengthWeaknessItem(
                title = o["title"]?.jsonPrimitive?.content ?: "",
                explanation = o["explanation"]?.jsonPrimitive?.content ?: "",
            )
        }
        val weaknesses = (obj["weaknesses"]?.jsonArray ?: emptyList()).map {
            val o = it.jsonObject
            StrengthWeaknessItem(
                title = o["title"]?.jsonPrimitive?.content ?: "",
                explanation = o["explanation"]?.jsonPrimitive?.content ?: "",
            )
        }
        val suggestions = (obj["improvementSuggestions"]?.jsonArray ?: emptyList()).map {
            val o = it.jsonObject
            ImprovementSuggestion(
                title = o["title"]?.jsonPrimitive?.content ?: "",
                explanation = o["explanation"]?.jsonPrimitive?.content ?: "",
            )
        }
        val missingKeywords = (obj["missingKeywords"]?.jsonArray ?: emptyList()).mapNotNull { it.jsonPrimitive.content }
        return CvAnalysis(
            id = "groq-${currentTimeMillis()}",
            fileName = fileName,
            uploadDate = currentTimeMillis(),
            targetRole = targetRole,
            atsScore = atsScore,
            profile = profile,
            strengths = strengths.ifEmpty { listOf(StrengthWeaknessItem("—", "Analizde belirtilmedi.")) },
            weaknesses = weaknesses.ifEmpty { listOf(StrengthWeaknessItem("—", "Analizde belirtilmedi.")) },
            improvementSuggestions = suggestions.ifEmpty { listOf(ImprovementSuggestion("—", "Öneri yok.")) },
            missingKeywords = missingKeywords,
        )
    }

    private fun fallbackAnalysis(fileName: String, targetRole: String): CvAnalysis {
        return CvAnalysis(
            id = "groq-fallback-${currentTimeMillis()}",
            fileName = fileName,
            uploadDate = currentTimeMillis(),
            targetRole = targetRole,
            atsScore = AtsScore(70, 70, 70, 70, 70, 70),
            profile = ExtractedProfile("—", null, null, emptyList(), null),
            strengths = listOf(StrengthWeaknessItem("Analiz tamamlanamadı", "JSON ayrıştırılamadı.")),
            weaknesses = emptyList(),
            improvementSuggestions = emptyList(),
            missingKeywords = emptyList(),
        )
    }
}
