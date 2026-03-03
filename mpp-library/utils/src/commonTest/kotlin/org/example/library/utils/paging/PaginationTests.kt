package org.example.library.utils.paging

import org.example.library.utils.state.RemoteState
import org.example.library.utils.state.RemoteStateData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class PaginationTests {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `pagination flow test`() = runTest {
        // канал для закидывания "ответов сервера" при проверке. null позволяем чтобы логику
        // ожидания сделать
        val channel = Channel<List<Int>?>()

        val pagination: Pagination<Int> = Pagination(
            dataSource = object : PagingDataSource<Int> {
                override fun isPageFull(list: List<Int>): Boolean {
                    return list.isNotEmpty()
                }

                override suspend fun loadPage(currentList: List<Int>?): List<Int> {
                    return channel
                        .receiveAsFlow()
                        .filterNotNull()
                        .first()
                }
            },
            itemKey = { it }
        )


        // изначально состояние загрузки
        assertIs<RemoteState.Loading>(pagination.state.value)

        // далее запускаем загрузку первой страницы. пока грузим - лоадинг должен быть
        pagination.paginationAction(
            action = { loadFirstPage() },
            channel = channel,
            response = listOf(0, 1, 2),
            onLoad = { assertIs<RemoteState.Loading>(it) }
        )

        // а как загрузили - данные должны быть
        pagination.state.value.let { state ->
            assertIs<RemoteState.Success<PagingState<Int>>>(state)
            assertEquals(
                expected = PagingState(
                    items = listOf(0, 1, 2),
                    isRefreshing = false,
                    isNextPageLoading = false,
                    isEndOfList = false,
                ),
                actual = state.data
            )
        }

        // далее грузим следующую страницу. пока идет загрузка у нас флаг загрузки новой страницы
        // должен быть тру
        pagination.paginationAction(
            action = { loadNextPage() },
            channel = channel,
            response = listOf(3, 4, 5),
            onLoad = { state ->
                assertIs<RemoteState.Success<PagingState<Int>>>(state)
                assertEquals(
                    expected = PagingState(
                        items = listOf(0, 1, 2),
                        isRefreshing = false,
                        isNextPageLoading = true,
                        isEndOfList = false,
                    ),
                    actual = state.data
                )
            }
        )

        // после завершения загрузки у нас список должен стать больше
        pagination.state.value.let { state ->
            assertIs<RemoteState.Success<PagingState<Int>>>(state)
            assertEquals(
                expected = PagingState(
                    items = listOf(0, 1, 2, 3, 4, 5),
                    isRefreshing = false,
                    isNextPageLoading = false,
                    isEndOfList = false,
                ),
                actual = state.data
            )
        }

        // далее делаем пулл ту рефреш - пока грузим флаг должен быть
        pagination.paginationAction(
            action = { refresh() },
            channel = channel,
            response = listOf(-1, 0, 1),
            onLoad = { state ->
                assertIs<RemoteState.Success<PagingState<Int>>>(state)
                assertEquals(
                    expected = PagingState(
                        items = listOf(0, 1, 2, 3, 4, 5),
                        isRefreshing = true,
                        isNextPageLoading = false,
                        isEndOfList = false,
                    ),
                    actual = state.data
                )
            }
        )

        // после подгрузки у нас список чуть расширится
        pagination.state.value.let { state ->
            assertIs<RemoteState.Success<PagingState<Int>>>(state)
            assertEquals(
                expected = PagingState(
                    items = listOf(-1, 0, 1, 2, 3, 4, 5),
                    isRefreshing = false,
                    isNextPageLoading = false,
                    isEndOfList = false,
                ),
                actual = state.data
            )
        }

        // далее грузим еще дальше список но новых страниц нет. пока идет загрузка опять же флаг
        // должен светиться
        pagination.paginationAction(
            action = { loadNextPage() },
            channel = channel,
            response = emptyList(),
            onLoad = { state ->
                assertIs<RemoteState.Success<PagingState<Int>>>(state)
                assertEquals(
                    expected = PagingState(
                        items = listOf(-1, 0, 1, 2, 3, 4, 5),
                        isRefreshing = false,
                        isNextPageLoading = true,
                        isEndOfList = false,
                    ),
                    actual = state.data
                )
            }
        )

        // загрузка завершена - список полностью загружен
        pagination.state.value.let { state ->
            assertIs<RemoteState.Success<PagingState<Int>>>(state)
            assertEquals(
                expected = PagingState(
                    items = listOf(-1, 0, 1, 2, 3, 4, 5),
                    isRefreshing = false,
                    isNextPageLoading = false,
                    isEndOfList = true,
                ),
                actual = state.data
            )
        }

        // далее делаем еще пулл ту рефреш - пока грузим флаг должен быть
        // но теперь рефреш выдаст вообще новые данные
        pagination.paginationAction(
            action = { refresh() },
            channel = channel,
            response = listOf(10, 11),
            onLoad = { state ->
                assertIs<RemoteState.Success<PagingState<Int>>>(state)
                assertEquals(
                    expected = PagingState(
                        items = listOf(-1, 0, 1, 2, 3, 4, 5),
                        isRefreshing = true,
                        isNextPageLoading = false,
                        isEndOfList = true,
                    ),
                    actual = state.data
                )
            }
        )

        // после подгрузки у нас список заменится и сбросится состояние завершившегося списка
        pagination.state.value.let { state ->
            assertIs<RemoteState.Success<PagingState<Int>>>(state)
            assertEquals(
                expected = PagingState(
                    items = listOf(10, 11),
                    isRefreshing = false,
                    isNextPageLoading = false,
                    isEndOfList = false,
                ),
                actual = state.data
            )
        }

        // далее перезагрузку данных с нуля
        pagination.paginationAction(
            action = { loadFirstPage() },
            channel = channel,
            response = listOf(1, 3),
            onLoad = { state ->
                assertIs<RemoteState.Loading>(state)
            }
        )

        // после загрузки просто новый список
        pagination.state.value.let { state ->
            assertIs<RemoteState.Success<PagingState<Int>>>(state)
            assertEquals(
                expected = PagingState(
                    items = listOf(1, 3),
                    isRefreshing = false,
                    isNextPageLoading = false,
                    isEndOfList = false,
                ),
                actual = state.data
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `replaceEverything strategy test`() = runTest {
        val channel = Channel<List<Int>?>()

        // Инициализируем с нужной стратегией
        val pagination: Pagination<Int> = Pagination(
            dataSource = object : PagingDataSource<Int> {
                override fun isPageFull(list: List<Int>): Boolean = list.isNotEmpty()

                override suspend fun loadPage(currentList: List<Int>?): List<Int> {
                    return channel.receiveAsFlow().filterNotNull().first()
                }
            },
            itemKey = { it },
            refreshStrategy = RefreshStrategy.ReplaceEverything
        )

        // 1. Загружаем первую страницу (исходные данные)
        pagination.paginationAction(
            action = { loadFirstPage() },
            channel = channel,
            response = listOf(1, 2, 3),
            onLoad = { assertIs<RemoteState.Loading>(it) }
        )

        // Проверяем, что исходные данные загрузились
        pagination.state.value.let { state ->
            assertIs<RemoteState.Success<PagingState<Int>>>(state)
            assertEquals(listOf(1, 2, 3), state.data.items)
        }

        // 2. Делаем Refresh с новыми данными
        pagination.paginationAction(
            action = { refresh() },
            channel = channel,
            // Возвращаем данные, которые полностью отличаются
            // (или пересекаются - для ReplaceEverything это неважно)
            response = listOf(4, 5),
            onLoad = { state ->
                assertIs<RemoteState.Success<PagingState<Int>>>(state)
                // Важный момент: во время загрузки (isRefreshing=true)
                // старые данные все еще отображаются
                assertEquals(
                    expected = PagingState(
                        items = listOf(1, 2, 3),
                        isRefreshing = true,
                        isNextPageLoading = false,
                        isEndOfList = false,
                    ),
                    actual = state.data
                )
            }
        )

        // 3. Проверяем финал: старые данные (1, 2, 3) должны исчезнуть, остаться только (4, 5)
        pagination.state.value.let { state ->
            assertIs<RemoteState.Success<PagingState<Int>>>(state)
            assertEquals(
                expected = PagingState(
                    items = listOf(4, 5), // Полная замена
                    isRefreshing = false,
                    isNextPageLoading = false,
                    isEndOfList = false,
                ),
                actual = state.data
            )
        }
    }

    // TODO тесты на множественный вызов действий
    //  https://github.com/icerockdev/moko-paging/blob/3f57c8f9112d9ed412946b70f3ffc850d784cb05/paging/src/commonTest/kotlin/dev/icerock/moko/paging/PaginationTest.kt#L88

    private suspend fun <Item> Pagination<Item>.paginationAction(
        action: suspend Pagination<Item>.() -> Unit,
        channel: Channel<List<Item>?>,
        response: List<Item>,
        onLoad: (RemoteStateData<PagingState<Item>>) -> Unit
    ) {
        val pagination = this
        coroutineScope {
            val result = async {
                pagination.action()
            }
            // дожидаемся момента когда "ждем ответа сервера"
            channel.send(null)
            // проверяем что в этот момент происходит
            onLoad(pagination.state.value)
            // отвечаем сервером
            channel.send(response)
            result.await()
        }
    }
}
