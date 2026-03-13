package com.alperenturker.cviews.util

/**
 * CV analiz geçmişi JSON olarak kaydedilir / yüklenir.
 * App kapat-aç sonrası geçmişin görünmesi için kullanılır.
 */
expect fun saveHistoryJson(json: String)

expect fun loadHistoryJson(): String?
