package com.alperenturker.cviews.data.pdf

/**
 * Extracts plain text from PDF bytes.
 * Returns null if extraction fails or PDF is empty.
 */
expect suspend fun extractTextFromPdf(pdfBytes: ByteArray): String?
