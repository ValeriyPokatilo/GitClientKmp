package org.example.library.utils

import kotlinx.datetime.LocalTime
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

/**
 * Форматирование длительности в формат "mm:ss".
 * Значения больше 59:59 секунд игнорируются.
 *
 * @receiver длительность которую нужно отформатировать
 * @return строка формата "mm:ss", в случае 0 секунд - пустая строка
 */
fun Duration.formatMinutesSeconds(): String {
    val duration: Duration = this
    if (duration.inWholeSeconds == 0L) return ""

    val minutes: Long = duration.inWholeMinutes
    val seconds: Long = (duration - minutes.minutes).inWholeSeconds

    return buildString {
        append(minutes.toString().padStart(2, '0'))
        append(':')
        append(seconds.toString().padStart(2, '0'))
    }
}

/**
 * Форматирует номера телефонов с кодом страны из 1 символа (как +7).
 *
 * Из 11 чисел делает строку вида +X (XXX) XXX-XX-XX
 */
@Suppress("MagicNumber")
fun String.formatPhone(): String {
    // Проверяем, что строка состоит только из цифр
    val digits = this.filter { it.isDigit() }

    // Проверяем, что длина строки корректная
    require(digits.length == 11) { "unsupported phone number format" }

    // Форматируем строку в вид +7 (XXX) XXX-XX-XX
    return buildString {
        append("+")
        append(digits[0])
        append(" (")
        append(digits.substring(1, 4))
        append(") ")
        append(digits.substring(4, 7))
        append("-")
        append(digits.substring(7, 9))
        append("-")
        append(digits.substring(9, 11))
    }
}

fun LocalTime.formatTime(): String {
    val hours: String = hour.toString().padStart(2)
    val minutes: String = minute.toString().padStart(2, '0')
    return "$hours:$minutes"
}
