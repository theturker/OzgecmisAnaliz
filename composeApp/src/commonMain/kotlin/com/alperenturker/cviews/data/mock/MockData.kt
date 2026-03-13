package com.alperenturker.cviews.data.mock

import com.alperenturker.cviews.currentTimeMillis
import com.alperenturker.cviews.domain.model.AtsScore
import com.alperenturker.cviews.domain.model.CvAnalysis
import com.alperenturker.cviews.domain.model.ExtractedProfile
import com.alperenturker.cviews.domain.model.ImprovementSuggestion
import com.alperenturker.cviews.domain.model.StrengthWeaknessItem

object MockData {

    val sampleAnalysis: CvAnalysis = CvAnalysis(
        id = "analysis-1",
        fileName = "John_Doe_CV.pdf",
        uploadDate = currentTimeMillis() - 86400000 * 2,
        targetRole = "Senior Android Developer",
        atsScore = AtsScore(
            total = 78,
            parsingScore = 85,
            keywordMatch = 72,
            structureScore = 80,
            readabilityScore = 75,
            impactScore = 82,
        ),
        profile = ExtractedProfile(
            name = "Ahmet Yılmaz",
            detectedRole = "Android Developer",
            yearsOfExperience = 5,
            topSkills = listOf("Kotlin", "Jetpack Compose", "Android SDK", "REST APIs", "Git"),
            education = "Bilgisayar Mühendisliği, İTÜ",
        ),
        strengths = listOf(
            StrengthWeaknessItem(
                title = "Güçlü teknik beceriler",
                explanation = "Kotlin ve modern Android stack konusunda iyi deneyim.",
            ),
            StrengthWeaknessItem(
                title = "Proje deneyimi",
                explanation = "Birden fazla production uygulaması yayınlanmış.",
            ),
            StrengthWeaknessItem(
                title = "Takım çalışması",
                explanation = "Agile metodolojiler ve code review deneyimi.",
            ),
        ),
        weaknesses = listOf(
            StrengthWeaknessItem(
                title = "CI/CD vurgusu eksik",
                explanation = "CI/CD pipeline ve otomasyon deneyimi özgeçmişte belirtilmemiş.",
            ),
            StrengthWeaknessItem(
                title = "Unit test bahsi az",
                explanation = "Test yazma ve TDD deneyimi daha net vurgulanabilir.",
            ),
        ),
        improvementSuggestions = listOf(
            ImprovementSuggestion(
                title = "Anahtar kelimeleri ekleyin",
                explanation = "İlan metnindeki 'Jetpack Compose', 'CI/CD', 'Unit Testing' gibi terimleri özgeçmişinize ekleyin.",
            ),
            ImprovementSuggestion(
                title = "Deneyim bölümünü güçlendirin",
                explanation = "Her rolde somut metrikler ve başarılar ekleyin (örn. performans iyileştirmesi %X).",
            ),
            ImprovementSuggestion(
                title = "Özet bölümünü hedefe göre uyarlayın",
                explanation = "Başvurduğunuz role göre ilk paragrafı özelleştirin.",
            ),
        ),
        missingKeywords = listOf(
            "Jetpack Compose",
            "CI/CD",
            "Unit Testing",
            "Modular Architecture",
            "Kotlin Coroutines",
            "Room Database",
        ),
    )

    val landingFeatures = listOf(
        "ATS Uyumluluk Skoru",
        "Yetenek Çıkarımı",
        "Deneyim Tespiti",
        "Eksik Anahtar Kelimeler",
        "İyileştirme Önerileri",
    )
}
