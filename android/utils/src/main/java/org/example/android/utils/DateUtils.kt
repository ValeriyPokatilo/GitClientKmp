package org.example.android.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

fun LocalDate.formatDate(): String {
    val day: String = dayOfMonth.toString().padStart(2, '0')
    val month: String = monthNumber.toString().padStart(2, '0')
    return "$day.$month.$year"
}

fun LocalTime.formatTime(): String {
    val hours: String = hour.toString().padStart(2)
    val minutes: String = minute.toString().padStart(2, '0')
    return "$hours:$minutes"
}
