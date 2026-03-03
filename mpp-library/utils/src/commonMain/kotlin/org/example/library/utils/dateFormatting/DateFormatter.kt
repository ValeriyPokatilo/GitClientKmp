package org.example.library.utils.dateFormatting

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

/** Модель форматирования дат
 *
 *  инстанс объекта нужно 1 раз на способ форматирования (например при создании вьюмодели), а потом реюзать форматтер.
 *  чтоб не было что в цикле списка элементов мы постоянно новый форматтер создаем
 *
 *  @param format строка, соответствующая формату даты, например "dd.MM.yyyy", "dd M yy"
 */
expect class DateFormatter(format: String) {
    fun format(date: LocalDate): String
    fun format(dateTime: LocalDateTime): String
    fun parse(value: String): LocalDate
}
