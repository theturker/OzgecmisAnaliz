package com.alperenturker.cviews.util

import com.alperenturker.cviews.appContext

private const val PREFS_NAME = "cv_rehberi_prefs"
private const val KEY_HISTORY_JSON = "history_json"

actual fun saveHistoryJson(json: String) {
    appContext?.getSharedPreferences(PREFS_NAME, 0)?.edit()?.putString(KEY_HISTORY_JSON, json)?.apply()
}

actual fun loadHistoryJson(): String? {
    return appContext?.getSharedPreferences(PREFS_NAME, 0)?.getString(KEY_HISTORY_JSON, null)
}
