package com.alperenturker.cviews.util

import platform.UIKit.UIPasteboard

actual fun putTextToClipboard(text: String) {
    val pasteboard = UIPasteboard.generalPasteboard
    pasteboard.setValue(text, forPasteboardType = "public.plain-text")
}
