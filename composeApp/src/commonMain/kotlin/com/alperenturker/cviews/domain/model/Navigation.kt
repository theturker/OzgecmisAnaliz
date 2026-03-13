package com.alperenturker.cviews.domain.model

/**
 * App navigation destinations.
 */
sealed class Screen {
    data object Landing : Screen()
    data object Upload : Screen()
    data object History : Screen()
    data class Analysis(val analysisId: String) : Screen()
}

fun Screen.toRoute(): String = when (this) {
    is Screen.Landing -> "landing"
    is Screen.Upload -> "upload"
    is Screen.History -> "history"
    is Screen.Analysis -> "analysis/$analysisId"
}

fun routeToScreen(route: String): Screen = when {
    route == "landing" -> Screen.Landing
    route == "upload" -> Screen.Upload
    route == "history" -> Screen.History
    route.startsWith("analysis/") -> Screen.Analysis(analysisId = route.removePrefix("analysis/"))
    else -> Screen.Landing
}
