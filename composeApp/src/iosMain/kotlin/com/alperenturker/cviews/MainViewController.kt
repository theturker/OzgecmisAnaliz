package com.alperenturker.cviews

import androidx.compose.ui.window.ComposeUIViewController

fun MainViewController() = ComposeUIViewController {
    App(
        selectedPdf = DocumentPickerBridge.selectedPdf,
        preExtractedPdfText = DocumentPickerBridge.extractedText,
        onFilePicked = { bytes, name -> DocumentPickerBridge.onDocumentPicked(bytes, name) },
        onRequestPickFile = { DocumentPickerBridge.documentPickerLauncher?.invoke() },
    )
}

/** Swift tarafından çağrılır: dosya seçiciyi göstermek için launcher kaydedilir. */
fun setDocumentPickerLauncher(launcher: () -> Unit) {
    DocumentPickerBridge.setDocumentPickerLauncher(launcher)
}

/** Swift tarafından çağrılır: kullanıcı dosya seçtiğinde (bytes, name). */
fun onDocumentPicked(bytes: ByteArray, name: String) {
    DocumentPickerBridge.onDocumentPicked(bytes, name)
}

/**
 * Swift tarafından çağrılır: seçilen dosyanın base64 içeriği.
 */
fun onDocumentPickedBase64(base64: String, name: String) {
    val bytes = kotlin.runCatching {
        kotlin.io.encoding.Base64.decode(base64)
    }.getOrNull() ?: return
    DocumentPickerBridge.onDocumentPicked(bytes, name)
}

/**
 * Swift tarafından çağrılır: base64 + PDFKit ile çıkarılmış metin (iOS analiz için).
 */
fun onDocumentPickedBase64WithExtractedText(base64: String, name: String, extractedText: String) {
    val bytes = kotlin.runCatching {
        kotlin.io.encoding.Base64.decode(base64)
    }.getOrNull() ?: return
    DocumentPickerBridge.onDocumentPickedWithExtractedText(bytes, name, extractedText)
}