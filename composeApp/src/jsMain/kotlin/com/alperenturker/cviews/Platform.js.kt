package com.alperenturker.cviews

class JsPlatform: Platform {
    override val name: String = "Web with Kotlin/JS"
}

actual fun getPlatform(): Platform = JsPlatform()

actual fun currentTimeMillis(): Long = kotlin.js.Date().getTime().toLong()