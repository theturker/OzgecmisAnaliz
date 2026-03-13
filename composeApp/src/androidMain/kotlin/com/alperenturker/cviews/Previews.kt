package com.alperenturker.cviews

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.alperenturker.cviews.data.mock.PreviewData
import com.alperenturker.cviews.presentation.analysis.AnalysisScreen
import com.alperenturker.cviews.presentation.history.HistoryScreen
import com.alperenturker.cviews.presentation.landing.LandingScreen
import com.alperenturker.cviews.presentation.ui.theme.CvRehberiTheme
import com.alperenturker.cviews.presentation.upload.UploadScreen

@Preview(name = "Landing", showBackground = true)
@Composable
private fun LandingScreenPreview() {
    CvRehberiTheme {
        LandingScreen(
            onUploadClick = {},
            onExampleAnalysisClick = {},
            onHistoryClick = {},
        )
    }
}

@Preview(name = "Upload", showBackground = true)
@Composable
private fun UploadScreenPreview() {
    CvRehberiTheme {
        UploadScreen(
            selectedFileName = null,
            onSelectFileClick = {},
            onAnalyzeClick = {},
            onBackClick = {},
        )
    }
}

@Preview(name = "Analysis", showBackground = true)
@Composable
private fun AnalysisScreenPreview() {
    CvRehberiTheme {
        AnalysisScreen(
            analysis = PreviewData.sampleAnalysis,
            onBackClick = {},
        )
    }
}

@Preview(name = "History", showBackground = true)
@Composable
private fun HistoryScreenPreview() {
    CvRehberiTheme {
        HistoryScreen(
            analyses = PreviewData.historyItems,
            onAnalysisClick = {},
            onDeleteAnalysis = {},
            onBackClick = {},
        )
    }
}
