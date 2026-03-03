package org.example.library.tests.di

import com.russhwolf.settings.MapSettings
import com.russhwolf.settings.Settings
import dev.icerock.moko.mvvm.test.TestViewModelScope
import dev.icerock.tests.utils.TestAntilog
import io.github.aakira.napier.Antilog
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandler
import io.ktor.client.engine.mock.respondBadRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.test.TestResult
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.koin.core.Koin
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Выполнить тест с использованием тестового Koin.
 *
 * Позволяет настроить данные в KeyValueStorage и настроить логику http ответов
 *
 * @param settings стартовые данные в Settings (KVS)
 * @param mockHandler логика обработки http запросов - что ответить на какой запрос
 * @param koinModules модули для приложения, которые будут переданы в Koin. При чем зависимости
 * Settings, HttpClientEngine будут заменены на тестовые.
 * @param testBody - тело теста. Как аргумент приходит настроенный Koin из которого можно получать
 * нужные объекты. В ресивере будет CoroutineScope, поэтому можно запускать async без создания
 * новых скоупов
 *
 * @return возвращает объект TestResult, также как runTest. Использовать в тестах также как и
 * runTest - через fun someTest() = runConfiguredTest
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun runConfiguredTest(
    settings: MapSettings = MapSettings(),
    mockHandler: MockRequestHandler = { respondBadRequest() },
    koinModules: List<Module>,
    testBody: suspend CoroutineScope.(Koin) -> Unit
): TestResult = runTest {
    // создаем Koin граф со всеми модулями приложения и заменой части зависостей на тестовые
    val koin: Koin = Koin().apply {
        // load application modules
        loadModules(koinModules)

        // override test mock modules
        loadModules(
            modules = listOf(
                testLoggerModule,
                getTestSettingsModule(settings),
                getTestHttpClientEngineModule(mockHandler)
            ),
            allowOverride = true
        )
    }

    // выставляем Main диспетчер для корутин такой чтоб сразу выполнял задачи синхронно
    val testDispatcher = UnconfinedTestDispatcher(testScheduler)
    Dispatchers.setMain(testDispatcher)

    // меняем ViewModelScope
    TestViewModelScope.setupViewModelScope(CoroutineScope(testDispatcher))

    // выполняем тело теста и вне зависимости от успеха или эксепшена мы должны закрыть созданный коин
    try {
        coroutineScope {
            testBody(koin)
        }
    } finally {
        koin.close()
        TestViewModelScope.resetViewModelScope()
        Dispatchers.resetMain()
    }
}

private fun getTestSettingsModule(
    settings: MapSettings,
): Module = module {
    single<Settings> { settings }
}

private fun getTestHttpClientEngineModule(
    mock: MockRequestHandler,
): Module = module {
    single<HttpClientEngine> { MockEngine(mock) }
}

private val testLoggerModule: Module = module {
    single<Antilog> { TestAntilog() }
}
