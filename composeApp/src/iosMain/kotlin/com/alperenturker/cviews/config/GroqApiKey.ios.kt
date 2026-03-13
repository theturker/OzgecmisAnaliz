package com.alperenturker.cviews.config

import platform.Foundation.NSBundle

private const val PLACEHOLDER = "YOUR_GROQ_API_KEY_HERE"

actual fun getGroqApiKey(): String {
    val value = NSBundle.mainBundle.objectForInfoDictionaryKey("GROQ_API_KEY")
    val s = (value as? String)?.trim() ?: ""
    return if (s.isBlank() || s == PLACEHOLDER) "" else s
}
