package com.alperenturker.cviews.util

import kotlinx.browser.window

actual fun putTextToClipboard(text: String) {
    window.navigator.asDynamic().clipboard?.writeText(text)
}
