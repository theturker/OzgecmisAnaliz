package com.alperenturker.cviews.config

actual fun getGroqApiKey(): String = System.getenv("GROQ_API_KEY") ?: ""
