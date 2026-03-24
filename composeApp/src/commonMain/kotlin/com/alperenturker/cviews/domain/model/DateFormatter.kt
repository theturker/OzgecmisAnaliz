package com.alperenturker.cviews.domain.model

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant as KotlinInstant

/**
 * Formats timestamp (millis since epoch) to "dd.MM.yyyy" display string.
 */
fun formatDateForDisplay(timestampMs: Long): String {
    val local = KotlinInstant.fromEpochMilliseconds(timestampMs)
        .toLocalDateTime(TimeZone.currentSystemDefault())
    val d = local.date.day.toString().padStart(2, '0')
    val m = (local.date.month.ordinal + 1).toString().padStart(2, '0')
    return "$d.$m.${local.date.year}"
}

/**
 * Formats timestamp to "dd.MM.yyyy HH:mm" (analiz tarihi ve saati).
 */
fun formatDateTimeForDisplay(timestampMs: Long): String {
    val local = KotlinInstant.fromEpochMilliseconds(timestampMs)
        .toLocalDateTime(TimeZone.currentSystemDefault())
    val d = local.date.day.toString().padStart(2, '0')
    val m = (local.date.month.ordinal + 1).toString().padStart(2, '0')
    val h = local.time.hour.toString().padStart(2, '0')
    val min = local.time.minute.toString().padStart(2, '0')
    return "$d.$m.${local.date.year} $h:$min"
}
