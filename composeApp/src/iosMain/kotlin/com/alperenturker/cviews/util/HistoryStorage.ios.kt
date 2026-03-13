package com.alperenturker.cviews.util

import platform.Foundation.NSUserDefaults

private const val KEY_HISTORY_JSON = "cv_rehberi_history_json"

actual fun saveHistoryJson(json: String) {
    NSUserDefaults.standardUserDefaults.setObject(json, KEY_HISTORY_JSON)
    NSUserDefaults.standardUserDefaults.synchronize()
}

actual fun loadHistoryJson(): String? {
    return NSUserDefaults.standardUserDefaults.stringForKey(KEY_HISTORY_JSON)
}
