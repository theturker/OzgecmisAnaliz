package com.alperenturker.cviews

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

/**
 * iOS: Swift document picker ile köprü. Launcher Swift tarafından set edilir,
 * seçilen dosya Swift'ten onDocumentPicked ile gelir.
 */
object DocumentPickerBridge {
    private var _pickedFile by mutableStateOf<Pair<ByteArray?, String?>?>(null)
    private var _extractedText by mutableStateOf<String?>(null)

    val selectedPdf: Pair<ByteArray?, String?>?
        get() = _pickedFile

    /** Swift PDFKit ile çıkarılmış metin; analizde kullanılır. */
    val extractedText: String?
        get() = _extractedText

    var documentPickerLauncher: (() -> Unit)? = null

    fun setDocumentPickerLauncher(launcher: () -> Unit) {
        documentPickerLauncher = launcher
    }

    fun onDocumentPicked(bytes: ByteArray, name: String) {
        MainScope().launch {
            _pickedFile = Pair(bytes, name)
            _extractedText = null
        }
    }

    fun onDocumentPickedWithExtractedText(bytes: ByteArray, name: String, text: String) {
        MainScope().launch {
            _pickedFile = Pair(bytes, name)
            _extractedText = text.takeIf { it.isNotBlank() }
        }
    }

    fun clearPickedFile() {
        _pickedFile = null
        _extractedText = null
    }
}
