package com.alperenturker.cviews.util

import kotlinx.browser.localStorage

private const val KEY_HISTORY_JSON = "cv_rehberi_history_json"

actual fun saveHistoryJson(json: String) {
    localStorage.setItem(KEY_HISTORY_JSON, json)
}

actual fun loadHistoryJson(): String? {
    return localStorage.getItem(KEY_HISTORY_JSON)
}
