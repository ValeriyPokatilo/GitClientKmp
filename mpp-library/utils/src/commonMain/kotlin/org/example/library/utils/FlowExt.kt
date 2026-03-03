package org.example.library.utils

import dev.icerock.moko.mvvm.flow.CMutableStateFlow
import dev.icerock.moko.mvvm.flow.CStateFlow
import dev.icerock.moko.mvvm.flow.cMutableStateFlow
import dev.icerock.moko.mvvm.flow.cStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

/**
 * Сокращенный вариант создания CStateFlow из Flow
 */
fun <T> Flow<T>.cStateIn(
    scope: CoroutineScope,
    started: SharingStarted = SharingStarted.Eagerly,
    initValue: T,
): CStateFlow<T> = this.stateIn(
    scope = scope,
    started = started,
    initialValue = initValue
).cStateFlow()

/*
 * Конструктор для упрощения создания с CMutableStateFlow
 */
fun <T> CMutableStateFlow(initValue: T): CMutableStateFlow<T> =
    MutableStateFlow(initValue).cMutableStateFlow()

/** расширение стандартного combine для flow (большее количество типизированных аргументов) */
@Suppress("MagicNumber")
public fun <T1, T2, T3, T4, T5, T6, R> combine(
    flow: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    transform: suspend (T1, T2, T3, T4, T5, T6) -> R,
): Flow<R> =
    kotlinx.coroutines.flow.combine(flow, flow2, flow3, flow4, flow5, flow6) { args: Array<*> ->
        @Suppress("UNCHECKED_CAST")
        transform(
            args[0] as T1,
            args[1] as T2,
            args[2] as T3,
            args[3] as T4,
            args[4] as T5,
            args[5] as T6
        )
    }
