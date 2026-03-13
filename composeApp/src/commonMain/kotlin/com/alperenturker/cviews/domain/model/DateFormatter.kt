package com.alperenturker.cviews.domain.model

/**
 * Formats timestamp (millis since epoch) to "dd.MM.yyyy" display string.
 * Uses simple arithmetic to avoid Java date APIs in common code.
 */
fun formatDateForDisplay(timestampMs: Long): String {
    val days = (timestampMs / (24 * 60 * 60 * 1000)).toInt()
    val (year, dayOfYear) = epochDaysToYearAndDay(days)
    val (month, day) = dayOfYearToMonthDay(dayOfYear, isLeapYear(year))
    val d = day.toString().padStart(2, '0')
    val m = month.toString().padStart(2, '0')
    return "$d.$m.$year"
}

/**
 * Formats timestamp to "dd.MM.yyyy HH:mm" (analiz tarihi ve saati).
 */
fun formatDateTimeForDisplay(timestampMs: Long): String {
    val base = timestampMs / 1000
    val days = (timestampMs / (24 * 60 * 60 * 1000)).toInt()
    val (year, dayOfYear) = epochDaysToYearAndDay(days)
    val (month, day) = dayOfYearToMonthDay(dayOfYear, isLeapYear(year))
    val d = day.toString().padStart(2, '0')
    val m = month.toString().padStart(2, '0')
    val secondsInDay = (base % (24 * 60 * 60)).toInt()
    val hour = secondsInDay / 3600
    val minute = (secondsInDay % 3600) / 60
    val h = hour.toString().padStart(2, '0')
    val min = minute.toString().padStart(2, '0')
    return "$d.$m.$year $h:$min"
}

private fun epochDaysToYearAndDay(daysSinceEpoch: Int): Pair<Int, Int> {
    var d = daysSinceEpoch
    if (d < 0) return 1970 to 1
    // 400 years = 146097 days; 100 years = 36524; 4 years = 1461; 1 year = 365 or 366
    val q400 = d / 146097
    d %= 146097
    val q100 = d / 36524
    d %= 36524
    val q4 = d / 1461
    d %= 1461
    val q1 = (d / 365).coerceAtMost(3)
    d %= 365
    val year = 1970 + 400 * q400 + 100 * q100 + 4 * q4 + q1
    val dayOfYear = d + 1
    return year to dayOfYear
}

private fun isLeapYear(y: Int): Boolean =
    (y % 4 == 0 && y % 100 != 0) || (y % 400 == 0)

private val daysInMonth = listOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

private fun dayOfYearToMonthDay(dayOfYear: Int, leap: Boolean): Pair<Int, Int> {
    val feb = if (leap) 29 else 28
    val days = listOf(31, feb, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
    var d = dayOfYear
    for (m in 1..12) {
        if (d <= days[m - 1]) return m to d
        d -= days[m - 1]
    }
    return 12 to 31
}
