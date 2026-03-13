package com.alperenturker.cviews.config

/**
 * Returns Groq API key for HTTP requests.
 * Android: from BuildConfig (local.properties GROQ_API_KEY).
 * Other platforms: override in actual or use env.
 */
expect fun getGroqApiKey(): String
