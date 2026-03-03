package org.example.library.utils.dateFormatting

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

actual class DateFormatter actual constructor(format: String) {
    private val dateFormat: DateFormat = SimpleDateFormat(format, Locale.getDefault())

    actual fun format(date: LocalDate): String {
        return dateFormat.format(date.javaDate)
    }

    actual fun format(dateTime: LocalDateTime): String {
        return dateFormat.format(dateTime.javaDate)
    }

    @Suppress("DEPRECATION")
    actual fun parse(value: String): LocalDate {
        val javaDate: Date = dateFormat.parse(value)
            ?: throw IllegalArgumentException("Cannot parse date: $value")

        return LocalDate(
            year = javaDate.year + 1900,
            monthNumber = javaDate.month + 1,
            dayOfMonth = javaDate.date
        )
    }
}

@Suppress("DEPRECATION")
private val LocalDate.javaDate: Date
    get() = Date(year - 1900, monthNumber - 1, dayOfMonth)

@Suppress("DEPRECATION")
private val LocalDateTime.javaDate: Date
    get() = Date(year - 1900, monthNumber - 1, dayOfMonth, hour, minute, second)
