package com.alperenturker.cviews

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform