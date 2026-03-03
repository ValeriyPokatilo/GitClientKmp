package org.example.android.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Класс ActionDebouncer предоставляет механизм блокировки множественных действий (например, нажатий)
 * в течение заданного промежутка времени. Может использоваться в UI для предотвращения
 * повторных кликов и многократного выполнения действий.
 * @property coroutineScope Скоуп корутин, используемый для задержки следующего разрешенного действия.
 * @property debounceState Состояние, указывающее, заблокировано ли в текущий момент выполнение действия.
 * @property delayMillis Задержка (в миллисекундах) между разрешенными действиями.
 */
@Stable
class ActionDebouncer(
    private val coroutineScope: CoroutineScope,
    private val debounceState: MutableState<Boolean>,
    private val delayMillis: Long
) {
    val canPerform: Boolean
        get() = !debounceState.value

    /**
     * Выполняет переданное действие, если оно доступно.
     * После выполнения действия оно блокируется на заданное количество миллисекунд.
     * @param action Действие, которое нужно выполнить.
     */
    fun performAction(action: () -> Unit) {
        if (!canPerform) return

        debounceState.value = true
        action()

        coroutineScope.launch {
            delay(delayMillis)
            debounceState.value = false
        }
    }
}

/**
 * Создаёт и запоминает экземпляр [ActionDebouncer], привязанный к жизненному циклу композиции.
 * @param delayMillis Время блокировки между действиями, по умолчанию 300 мс.
 * @return Экземпляр [ActionDebouncer], который можно использовать для защиты от повторных кликов и других действий.
 */
@Composable
fun rememberActionDebouncer(
    delayMillis: Long = 300L
): ActionDebouncer {
    val scope = rememberCoroutineScope()
    val debounceState = remember { mutableStateOf(false) }

    return remember(scope, debounceState) {
        ActionDebouncer(
            coroutineScope = scope,
            debounceState = debounceState,
            delayMillis = delayMillis
        )
    }
}
