package com.alperenturker.cviews.util

import java.io.File

private val historyFile: File by lazy {
    File(System.getProperty("user.home"), ".cv_rehberi_history.json")
}

actual fun saveHistoryJson(json: String) {
    historyFile.writeText(json)
}

actual fun loadHistoryJson(): String? {
    return if (historyFile.exists()) historyFile.readText() else null
}
