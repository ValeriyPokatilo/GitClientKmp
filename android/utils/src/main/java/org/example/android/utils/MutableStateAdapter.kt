package org.example.android.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import dev.icerock.moko.mvvm.flow.compose.MutableStateAdapter

/**
 * Адаптер для преобразования связки value/onChange, используемые в юнитах в общем коде
 * в MutableState, используемый в ui компонентах
 *
 * @param value представляет текущее значение
 * @param onChange коллбэк для изменения значения
 * @return значение, обернутое в MutableState
 */
@Composable
fun <T> adapterMutableStateOf(value: T, onChange: (T) -> Unit): MutableState<T> {
    val state: State<T> = rememberUpdatedState(value)
    return remember(state, onChange) {
        MutableStateAdapter(
            state = state,
            mutate = onChange
        )
    }
}
