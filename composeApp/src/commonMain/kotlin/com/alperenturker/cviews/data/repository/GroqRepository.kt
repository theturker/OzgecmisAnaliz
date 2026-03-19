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

    /**
     * Analiz çıktısından yola çıkarak kullanıcının kopyalayıp düzenleyebileceği bir CV taslağı üretir.
     *
     * Önemli: Uygulama "minimum işlev" uyarısını azaltmak için kullanıcıya doğrudan kullanılabilir bir çıktı vermeyi hedefler.
     */
    suspend fun generateImprovedCv(analysis: CvAnalysis): Result<String> = withContext(Dispatchers.Default) {
        val apiKey = getGroqApiKey()
        if (apiKey.isBlank()) {
            return@withContext Result.success(buildFallbackImprovedCv(analysis, apiUsed = false))
        }

        val targetRole = analysis.targetRole ?: "Hedef rol belirtilmemiş"

        val systemPrompt = """
Sen bir CV editörü ve ATS uyumluluğu uzmanısın.
Aşağıdaki analiz verilerini kullanarak kullanıcı için "iyileştirilmiş CV taslağı" oluştur.
Amaç: Kullanıcı metnini kopyalayıp düzenleyebilmeli ve başvuruda kullanabilmeli.

Dil: Türkçe.
Ton: Profesyonel ve somut.

Çıktı SADECE aşağıdaki JSON formatında olmalı. Başka metin ekleme:
{
  "improvedCvText": ""
}

improvedCvText içinde önerilen format:
1) Başlık: <İsim> - <Hedef Rol>
2) Kısa Özet (3-4 cümle)
3) Beceri Seti (madde liste)
4) Deneyim (3-4 madde; ölçülebilir etki varsa ekle, yoksa makul genellemeler kullan)
5) Eğitim (varsa)
6) Anahtar Kelimeler (eksik anahtar kelimelerden derlenmiş madde listesi)
        """.trimIndent()

        val userContent = buildString {
            append("Hedef rol: $targetRole\n\n")
            append("Profil:\n")
            append("- İsim: ${analysis.profile.name}\n")
            append("- Tespit edilen rol: ${analysis.profile.detectedRole ?: "—"}\n")
            append("- Deneyim: ${analysis.profile.yearsOfExperience?.let { "$it yıl" } ?: "—"}\n")
            append("- Eğitim: ${analysis.profile.education ?: "—"}\n")
            append("- Üst beceriler: ${analysis.profile.topSkills.joinToString(", ")}\n\n")

            append("Güçlü yönler:\n")
            analysis.strengths.take(6).forEach { append("- ${it.title}\n") }
            append("\n")

            append("İyileştirme önerileri:\n")
            analysis.improvementSuggestions.take(8).forEach { append("- ${it.title}: ${it.explanation}\n") }
            append("\n")

            append("Eksik anahtar kelimeler:\n")
            if (analysis.missingKeywords.isEmpty()) append("- —\n")
            else analysis.missingKeywords.take(20).forEach { append("- $it\n") }
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

            val raw = content.trim()
                .removePrefix("```json")
                .removePrefix("```")
                .removeSuffix("```")
                .trim()

            val obj = json.parseToJsonElement(raw).jsonObject
            val improved = obj["improvedCvText"]?.jsonPrimitive?.content
                ?: return@withContext Result.failure(RuntimeException("improvedCvText alanı bulunamadı."))

            Result.success(improved)
        } catch (e: Exception) {
            // İnceleme sürecinde internet / API erişimi sorunları olabileceğinden,
            // en azından mevcut analiz verilerinden işe yarar bir taslak döndürürüz.
            Result.success(buildFallbackImprovedCv(analysis, apiUsed = false))
        }
    }

    private fun buildFallbackImprovedCv(analysis: CvAnalysis, apiUsed: Boolean): String {
        val name = analysis.profile.name.takeIf { it.isNotBlank() } ?: "Kullanıcı"
        val targetRole = analysis.targetRole ?: analysis.profile.detectedRole ?: "Hedef Rol"
        val years = analysis.profile.yearsOfExperience?.let { "$it yıl" } ?: "—"
        val education = analysis.profile.education ?: "—"

        val skills = (analysis.profile.topSkills + analysis.missingKeywords)
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .distinct()
            .take(18)

        val strengthsBullets = analysis.strengths.take(4).ifEmpty {
            listOf(com.alperenturker.cviews.domain.model.StrengthWeaknessItem("Güçlü yön", "Somut katkılar vurgulanacaktır."))
        }

        val experienceBullets = strengthsBullets.map { s ->
            val expl = s.explanation.ifBlank { "Uygun projeler üzerinde katkı sağlandı." }
            "- ${s.title}: ${expl}"
        }

        val missingKeywords = analysis.missingKeywords.take(20)

        val fallbackNote = if (apiUsed) "" else "Not: Bu taslak, Groq API kullanılamadığında analiz verilerinden otomatik şablonla oluşturulmuştur.\n\n"

        return """
$fallbackNote${name} - ${targetRole}

Kısa Özet
${targetRole} alanında ${years} deneyime sahip. Güçlü teknik becerileri ve rolün gerektirdiği anahtar kelimeleri kullanarak ATS uyumluluğunu güçlendirmeye odaklanır.
Başvuru metnini somut çıktılarla destekler ve eksik anahtar kelimeleri uygun şekilde ekler.

Beceri Seti
${skills.takeIf { it.isNotEmpty() }?.joinToString("\n") { "- $it" } ?: "- —"}

Deneyim
${experienceBullets.joinToString("\n")}

Eğitim
$education

Anahtar Kelimeler
${if (missingKeywords.isEmpty()) "- —" else missingKeywords.joinToString("\n") { "- $it" }}
        """.trimIndent()
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
