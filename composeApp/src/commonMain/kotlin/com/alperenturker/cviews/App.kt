package com.alperenturker.cviews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alperenturker.cviews.presentation.navigation.AppNavigation
import com.alperenturker.cviews.presentation.ui.theme.CvRehberiTheme

@Composable
fun App(
    selectedPdf: Pair<ByteArray?, String?>? = null,
    /** iOS: Swift PDFKit ile çıkarılmış metin; yoksa null. */
    preExtractedPdfText: String? = null,
    onFilePicked: (ByteArray, String) -> Unit = { _, _ -> },
    onRequestPickFile: () -> Unit = {},
) {
    CvRehberiTheme {
        val surfaceColor = MaterialTheme.colorScheme.surface
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(surfaceColor),
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing),
                color = surfaceColor,
            ) {
                AppNavigation(
                selectedPdf = selectedPdf,
                preExtractedPdfText = preExtractedPdfText,
                onFilePicked = onFilePicked,
                onRequestPickFile = onRequestPickFile,
            )
            }
        }
    }
}
