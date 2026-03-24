package com.alperenturker.cviews.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.alperenturker.cviews.currentTimeMillis
import com.alperenturker.cviews.data.pdf.extractTextFromPdf
import com.alperenturker.cviews.data.repository.GroqRepository
import com.alperenturker.cviews.domain.model.CvAnalysis
import com.alperenturker.cviews.domain.model.Screen
import com.alperenturker.cviews.domain.model.routeToScreen
import com.alperenturker.cviews.domain.model.toRoute
import com.alperenturker.cviews.presentation.ui.components.BottomNavBar
import com.alperenturker.cviews.presentation.ui.components.BottomTab
import com.alperenturker.cviews.presentation.analysis.AnalysisScreen
import com.alperenturker.cviews.presentation.history.HistoryScreen
import com.alperenturker.cviews.presentation.landing.LandingScreen
import com.alperenturker.cviews.presentation.upload.UploadScreen
import com.alperenturker.cviews.util.saveHistoryJson
import com.alperenturker.cviews.util.loadHistoryJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.ui.Modifier
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

private val historyJson = Json { ignoreUnknownKeys = true }
private val analysisListSerializer = ListSerializer(CvAnalysis.serializer())
private const val ANALYSIS_COOLDOWN_MS = 48 * 60 * 60 * 1000L

private fun saveAnalyses(list: List<CvAnalysis>) {
    saveHistoryJson(historyJson.encodeToString(analysisListSerializer, list))
}

@Composable
fun AppNavigation(
    startScreen: Screen = Screen.Landing,
    selectedPdf: Pair<ByteArray?, String?>? = null,
    /** iOS: Swift PDFKit ile çıkarılmış metin; null ise extractTextFromPdf kullanılır. */
    preExtractedPdfText: String? = null,
    onFilePicked: (ByteArray, String) -> Unit = { _, _ -> },
    onRequestPickFile: () -> Unit = {},
) {
    var currentRoute by rememberSaveable { mutableStateOf(startScreen.toRoute()) }
    val currentScreen = routeToScreen(currentRoute)
    val analyses = remember { mutableStateListOf<CvAnalysis>() }
    val scope = rememberCoroutineScope()
    var nowMs by remember { mutableStateOf(currentTimeMillis()) }
    var analysisInProgress by remember { mutableStateOf(false) }
    var analysisError by remember { mutableStateOf<String?>(null) }
    val groqRepo = remember { GroqRepository() }
    var draftInProgress by remember { mutableStateOf(false) }
    var draftError by remember { mutableStateOf<String?>(null) }
    var draftAnalysisId by remember { mutableStateOf<String?>(null) }
    // Analiz listesinde olmayan sample analysis için taslak saklarız.
    val generatedDrafts = remember { mutableStateMapOf<String, String>() }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.Default) {
            loadHistoryJson()?.let { json ->
                runCatching {
                    historyJson.decodeFromString(analysisListSerializer, json)
                }.getOrNull()?.let { loaded ->
                    analyses.clear()
                    analyses.addAll(loaded)
                }
            }
        }
    }
    LaunchedEffect(Unit) {
        while (true) {
            nowMs = currentTimeMillis()
            delay(1000)
        }
    }

    val latestAnalysisTime = analyses.maxOfOrNull { it.uploadDate }
    val remainingCooldownMs = latestAnalysisTime
        ?.let { (it + ANALYSIS_COOLDOWN_MS - nowMs).coerceAtLeast(0L) }
        ?: 0L
    val canAnalyzeNow = remainingCooldownMs == 0L

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f)) {
            when (val screen = currentScreen) {
                is Screen.Landing -> LandingScreen(
                    onUploadClick = { currentRoute = Screen.Upload.toRoute() },
                    onHistoryClick = { currentRoute = Screen.History.toRoute() },
                )
                is Screen.Upload -> UploadScreen(
                    selectedFileName = selectedPdf?.second,
                    onSelectFileClick = onRequestPickFile,
                    onAnalyzeClick = { targetRole ->
                        val (bytes, name) = selectedPdf ?: return@UploadScreen
                        if (bytes == null) return@UploadScreen
                        if (targetRole.isNullOrBlank()) return@UploadScreen
                        if (!canAnalyzeNow) {
                            analysisError = "Yeni analiz için ${formatDuration(remainingCooldownMs)} bekleyin."
                            return@UploadScreen
                        }
                        analysisError = null
                        analysisInProgress = true
                        scope.launch {
                            val text = preExtractedPdfText?.takeIf { it.isNotBlank() }
                                ?: extractTextFromPdf(bytes)
                            if (text.isNullOrBlank()) {
                                analysisInProgress = false
                                analysisError = "PDF metni çıkarılamadı. Dosyanın metin içerdiğinden emin olun."
                                return@launch
                            }
                            groqRepo.analyzeCv(text, name ?: "document.pdf", targetRole)
                                .onSuccess { result ->
                                    analysisInProgress = false
                                    analyses.add(0, result)
                                    saveAnalyses(analyses.toList())
                                    currentRoute = Screen.Analysis(result.id).toRoute()
                                }
                                .onFailure { e ->
                                    analysisInProgress = false
                                    analysisError = e.message ?: "Analiz başarısız."
                                }
                        }
                    },
                    onBackClick = { currentRoute = Screen.Landing.toRoute() },
                    isAnalyzing = analysisInProgress,
                    analysisError = analysisError,
                    canAnalyzeNow = canAnalyzeNow,
                    remainingCooldownMs = remainingCooldownMs,
                )
                is Screen.History -> HistoryScreen(
                    analyses = analyses,
                    onAnalysisClick = { currentRoute = Screen.Analysis(it.id).toRoute() },
                    onDeleteAnalysis = { analysis ->
                        analyses.remove(analysis)
                        saveAnalyses(analyses.toList())
                    },
                    onBackClick = { currentRoute = Screen.Landing.toRoute() },
                )
                is Screen.Analysis -> {
                    val analysis = analyses.find { it.id == screen.analysisId }
                    val draftText = analysis?.improvedCvText ?: analysis?.id?.let { generatedDrafts[it] }
                    val showDraftProgress = analysis != null && draftInProgress && draftAnalysisId == analysis.id

                    AnalysisScreen(
                        analysis = analysis,
                        onBackClick = { currentRoute = Screen.Landing.toRoute() },
                        onCreateAnalysisClick = { currentRoute = Screen.Upload.toRoute() },
                        draftText = draftText,
                        isDraftInProgress = showDraftProgress,
                        draftError = draftError,
                        onGenerateDraftClick = {
                            if (analysis != null && !draftInProgress) {
                                draftError = null
                                draftInProgress = true
                                draftAnalysisId = analysis.id

                                scope.launch {
                                    groqRepo.generateImprovedCv(analysis)
                                        .onSuccess { improved ->
                                            draftInProgress = false
                                            generatedDrafts.remove(analysis.id)

                                            val idx = analyses.indexOfFirst { it.id == analysis.id }
                                            if (idx >= 0) {
                                                analyses[idx] = analyses[idx].copy(improvedCvText = improved)
                                                saveAnalyses(analyses.toList())
                                            } else {
                                                generatedDrafts[analysis.id] = improved
                                            }
                                        }
                                        .onFailure { e ->
                                            draftInProgress = false
                                            draftError = e.message ?: "Taslak üretilemedi."
                                        }
                                }
                            }
                        },
                    )
                }
            }
        }

        val selectedTab = when (val screen = currentScreen) {
            is Screen.Upload -> BottomTab.Upload
            is Screen.History -> BottomTab.History
            is Screen.Analysis -> BottomTab.Analysis
            is Screen.Landing -> BottomTab.Upload
        }

        BottomNavBar(
            selectedTab = selectedTab,
            onTabSelected = { tab ->
                when (tab) {
                    BottomTab.Upload -> currentRoute = Screen.Upload.toRoute()
                    BottomTab.Analysis -> {
                        val targetId = analyses.firstOrNull()?.id ?: "no-analysis"
                        currentRoute = Screen.Analysis(targetId).toRoute()
                    }
                    BottomTab.History -> currentRoute = Screen.History.toRoute()
                }
            },
        )
    }
}

private fun formatDuration(remainingMs: Long): String {
    val totalSeconds = (remainingMs / 1000).coerceAtLeast(0L)
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return "${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
}
