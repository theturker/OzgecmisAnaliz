package com.alperenturker.cviews.domain.model

import kotlinx.serialization.Serializable

/**
 * Full CV analysis result.
 */
@Serializable
data class CvAnalysis(
    val id: String,
    val fileName: String,
    val uploadDate: Long,
    val targetRole: String?,
    val atsScore: AtsScore,
    val profile: ExtractedProfile,
    val strengths: List<StrengthWeaknessItem>,
    val weaknesses: List<StrengthWeaknessItem>,
    val improvementSuggestions: List<ImprovementSuggestion>,
    val missingKeywords: List<String>,
)

@Serializable
data class AtsScore(
    val total: Int,
    val parsingScore: Int,
    val keywordMatch: Int,
    val structureScore: Int,
    val readabilityScore: Int,
    val impactScore: Int,
)

@Serializable
data class ExtractedProfile(
    val name: String,
    val detectedRole: String?,
    val yearsOfExperience: Int?,
    val topSkills: List<String>,
    val education: String?,
)

@Serializable
data class StrengthWeaknessItem(
    val title: String,
    val explanation: String,
    val iconName: String = "default",
)

@Serializable
data class ImprovementSuggestion(
    val title: String,
    val explanation: String,
)
