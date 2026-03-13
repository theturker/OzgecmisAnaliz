package com.alperenturker.cviews

/**
 * Returns current time in milliseconds since epoch.
 * Actual implementations are in androidMain, iosMain, jvmMain, etc.
 */
expect fun currentTimeMillis(): Long
