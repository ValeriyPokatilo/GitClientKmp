package org.example.library.tests.utils

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList

/**
 * Выполнить какую-то работу в ViewModel'и, которая должна выдать в результате экшен на UI.
 * По завершению работы получим экшен для проверки.
 *
 * @param flow поток с экшенами (`viewModel.actions`)
 * @param block действия с вьюмоделью по итогу которых ожидаем что будет 1 экшен выброшен
 *
 * @return экшен который выбросила viewmodel
 */
suspend fun <T> awaitAction(flow: Flow<T>, block: suspend () -> Unit): T {
    return awaitActions(flow, count = 1, block).single()
}

/**
 * Выполнить какую-то работу в ViewModel'и, которая должна выдать в результате некоторое количество
 * экшенов на UI. По завершению работы получим список полученных экшенов для проверки.
 *
 * @param flow поток с экшенами (`viewModel.actions`)
 * @param count сколько экшенов ожидаем
 * @param block действия с вьюмоделью по итогу которых ожидаем что будет `count` экшенов выброшено
 *
 * @return список экшенов который выбросила viewmodel
 */
suspend fun <T> awaitActions(flow: Flow<T>, count: Int, block: suspend () -> Unit): List<T> {
    return coroutineScope {
        val actions = async {
            flow.take(count)
                .toList()
        }

        block()

        actions.await()
    }
}
