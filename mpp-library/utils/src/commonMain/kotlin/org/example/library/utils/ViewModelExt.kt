package org.example.library.utils

import dev.icerock.moko.errors.mappers.mapThrowable
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import dev.icerock.moko.resources.desc.StringDesc
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/*
 * экстеншн для упрощения работы с отправкой событий из ViewModel
 */
fun <T> ViewModel.sendAction(
    actions: Channel<T>,
    action: T,
) {
    viewModelScope.launch {
        actions.send(action)
    }
}

/**
 * Запустить асинхронную работу с обработкой ошибок.
 *
 * В случае если произойдет ошибка - будет выброшен экшен, который создается лямбдой errorAction
 *
 * @param loading состояние загрузки. можно указать null или не указывать совсем. Если же указать
 * объект - у него будет выставлен флаг true на время пока идет асинхронная работа
 * @param actions канал действий, куда будет выбрашено действие показа ошибки
 * @param errorToAction фабрика действия показа ошибки
 * @param block блок с асинхронной работой для выполнения
 */
fun <Actions> ViewModel.launchGuard(
    actions: Channel<Actions>,
    errorToAction: (StringDesc) -> Actions,
    loading: MutableStateFlow<Boolean>? = null,
    onError: (Exception) -> Unit = {},
    block: suspend () -> Unit
) {
    viewModelScope.launch {
        try {
            loading?.value = true

            block()
        } catch (exception: Exception) {
            onError(exception)
            sendAction(
                actions,
                errorToAction(exception.mapThrowable())
            )
        } finally {
            loading?.value = false
        }
    }
}
