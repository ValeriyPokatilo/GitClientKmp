package org.example.library.utils.fields

import dev.icerock.moko.fields.core.FormField
import dev.icerock.moko.fields.core.validations.ValidationResult
import dev.icerock.moko.resources.desc.StringDesc
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import dev.icerock.moko.fields.flow.FormField as FlowFormField

/**
 * Исправленная версия валидации списка полей - в библиотеке валидация останавливается на первом
 * невалидном поле, а нам надо чтобы все поля провалидировались
 */
fun List<FormField<*, *>>.validate(): Boolean {
    // at first we should validate all fields and then check that all validation successful
    return map { it.validate() }.all { it }
}

/**
 * Валидация поля с возможностью делать связку с другими flow.
 *
 * Главный юзкейс - поля с подтверждением ввода (где есть 2 поля, например пароль и подтверджение
 * пароля).
 * В таком случае нужно чтобы мы слушали и изменение нашего поля и изменение зависимого поля, и
 * обновляли валидацию при каждом изменении.
 *
 * Приоритет в ошибках отдается правилам комбинации. То есть если combined flow выдал ошибку - она
 * будет выдана как результат валидации.
 * Например в кейсе с паролями - ошибка "пароли не совпадают" будет в приоритете.
 *
 * @param combined лямбда в которой можно скомбинировать текущий Flow с данными с другими Flow и
 * выдать Flow с ошибкой.
 * @param block правила валидации
 */
fun <D> fieldValidation(
    combined: (Flow<D>) -> Flow<StringDesc?>,
    block: ValidationResult<D>.() -> ValidationResult<D>
): ((Flow<D>) -> Flow<StringDesc?>) = { data ->
    combine(
        combined(data),
        data.map { dev.icerock.moko.fields.core.validations.fieldValidation(block)(it) }
    ) { combinedError, validationError ->
        combinedError ?: validationError
    }
}

/**
 * Настройка правил валидации для полей moko-fields. Вынесена сюда чтобы в одном файле можно было
 * использовать и вариант с combined (которого нет в moko-fields) и обычный, без проблем с импортами.
 *
 * @param block правила валидации
 */
fun <D> fieldValidation(
    block: ValidationResult<D>.() -> ValidationResult<D>
): ((Flow<D>) -> Flow<StringDesc?>) =
    dev.icerock.moko.fields.flow.validations.fieldValidation(block)

/**
 * Правило валидации проверяющее на равенство с другими данными.
 *
 * Если данные нашего поля и secondField не равны - будет выдана ошибка
 *
 * @param secondField с какоим полем сравнивать данные
 * @param notEqualText какую ошибку выдавать если данные не равны
 */
fun <D> equalValidation(
    secondField: Flow<D>,
    notEqualText: StringDesc
): (Flow<D>) -> Flow<StringDesc?> = { firstField ->
    combine(firstField, secondField) { first, second ->
        if (first != second) notEqualText else null
    }
}

fun <E> FlowFormField<String, E>.limitLength(scope: CoroutineScope, maxLength: Int) {
    scope.launch {
        this@limitLength.data.collect { value ->
            if (value.length > maxLength) {
                this@limitLength.setValue(value.take(maxLength))
            }
        }
    }
}

fun <E> FlowFormField<String, E>.numberOnly(scope: CoroutineScope) {
    scope.launch {
        this@numberOnly.data.collect { value ->
            if (!value.all { it.isDigit() }) {
                this@numberOnly.setValue(value.filter { it.isDigit() })
            }
        }
    }
}
