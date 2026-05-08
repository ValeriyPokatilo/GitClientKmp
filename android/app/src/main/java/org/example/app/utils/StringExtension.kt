package org.example.app.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime

fun String.toShortDate(): String {
    return runCatching {
        val instant: Instant = Instant.parse(this)
        val now: Instant = Clock.System.now()

        val timeZone: TimeZone = TimeZone.currentSystemDefault()

        val date: LocalDate = instant.toLocalDateTime(timeZone).date
        val currentYear: Int = now.toLocalDateTime(timeZone).year

        val formatter: DateTimeFormat<LocalDate> = LocalDate.Format {
            dayOfMonth()
            char(' ')
            monthName(MonthNames.ENGLISH_ABBREVIATED)

            if (date.year != currentYear) {
                char(' ')
                year()
            }
        }

        date.format(formatter)
    }.getOrElse { this }
}
