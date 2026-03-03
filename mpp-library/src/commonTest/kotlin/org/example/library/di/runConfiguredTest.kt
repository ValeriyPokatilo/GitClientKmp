package org.example.library.di

import com.russhwolf.settings.MapSettings
import io.ktor.client.engine.mock.MockRequestHandler
import io.ktor.client.engine.mock.respondBadRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.TestResult
import org.koin.core.Koin

/**
 * Выполнить тест с использованием тестового Koin.
 *
 * Позволяет настроить данные в KeyValueStorage и настроить логику http ответов
 *
 * @param settings стартовые данные в Settings (KVS)
 * @param mockHandler логика обработки http запросов - что ответить на какой запрос
 * @param testBody - тело теста. Как аргумент приходит настроенный Koin из которого можно получать
 * нужные объекты. В ресивере будет CoroutineScope, поэтому можно запускать async без создания
 * новых скоупов
 *
 * @return возвращает объект TestResult, также как runTest. Использовать в тестах также как и
 * runTest - через fun someTest() = runConfiguredTest
 */
fun runConfiguredTest(
    settings: MapSettings = MapSettings(),
    mockHandler: MockRequestHandler = { respondBadRequest() },
    testBody: suspend CoroutineScope.(Koin) -> Unit
): TestResult = org.example.library.tests.di.runConfiguredTest(
    settings = settings,
    mockHandler = mockHandler,
    koinModules = registerKoinModules(
        baseUrl = "http://localhost/"
    ),
    testBody = testBody
)
