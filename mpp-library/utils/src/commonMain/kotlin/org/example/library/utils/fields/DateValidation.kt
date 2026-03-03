package org.example.library.utils.fields

import dev.icerock.moko.fields.core.validations.ValidationResult
import dev.icerock.moko.resources.desc.StringDesc
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.example.library.utils.dateFormatting.DateFormatter

fun ValidationResult<String>.isDate(
    errorText: StringDesc,
    shouldBeInPast: Boolean,
): ValidationResult<String> {
    val formatter = DateFormatter("dd.MM.yyyy")
    return nextValidation { value ->
        try {
            val date: LocalDate = formatter.parse(value)
            val day = value.substring(DAY_START, DAY_END).toInt()
            val month = value.substring(MONTH_START, MONTH_END).toInt()
            val today: LocalDate =
                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

            val isValid = if (shouldBeInPast) {
                date < today && day <= MAX_DAY && month <= MAX_MONTH
            } else {
                date >= today && day <= MAX_DAY && month <= MAX_MONTH
            }

            if (isValid) {
                ValidationResult.success(value)
            } else {
                ValidationResult.failure(errorText)
            }
        } catch (_: Exception) {
            ValidationResult.failure(errorText)
        }
    }
}

private const val MAX_DAY = 31
private const val MAX_MONTH = 12
private const val DAY_START = 0
private const val DAY_END = 2
private const val MONTH_START = 3
private const val MONTH_END = 5
