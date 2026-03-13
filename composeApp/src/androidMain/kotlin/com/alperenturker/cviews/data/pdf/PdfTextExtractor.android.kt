package com.alperenturker.cviews.data.pdf

import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream

object PdfBoxHolder {
    private var initialized = false
    fun init(ctx: android.content.Context) {
        if (!initialized) {
            PDFBoxResourceLoader.init(ctx.applicationContext)
            initialized = true
        }
    }
}

actual suspend fun extractTextFromPdf(pdfBytes: ByteArray): String? = withContext(Dispatchers.IO) {
    try {
        ByteArrayInputStream(pdfBytes).use { input ->
            PDDocument.load(input).use { doc ->
                PDFTextStripper().getText(doc)
            }
        }.takeIf { it.isNotBlank() }
    } catch (_: Exception) {
        null
    }
}
