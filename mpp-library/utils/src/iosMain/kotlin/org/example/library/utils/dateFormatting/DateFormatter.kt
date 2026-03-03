package org.example.library.utils.dateFormatting

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toNSDateComponents
import platform.Foundation.NSCalendar
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale

actual class DateFormatter actual constructor(format: String) {
    private val dateFormat: NSDateFormatter = NSDateFormatter().apply {
        locale = NSLocale.currentLocale
        dateFormat = format
    }

    actual fun format(date: LocalDate): String {
        return dateFormat.stringFromDate(date.nsDate)
    }

    actual fun format(dateTime: LocalDateTime): String {
        return dateFormat.stringFromDate(dateTime.nsDate)
    }

    actual fun parse(value: String): LocalDate {
        val nsDate = dateFormat.dateFromString(value)
            ?: throw IllegalArgumentException("Cannot parse date: $value")

        return nsDate.toLocalDate()
    }
}

private val LocalDate.nsDate: NSDate
    get() = NSCalendar.currentCalendar.dateFromComponents(this.toNSDateComponents())!!

private val LocalDateTime.nsDate: NSDate
    get() = NSCalendar.currentCalendar.dateFromComponents(this.toNSDateComponents())!!
