package org.example.library.utils.paging

import kotlin.test.Test
import kotlin.test.assertEquals

class PagesCalcTest {

    @Test
    fun emptyListReturnsZeroPages() {
        // Пустой список (null или 0 элементов) должен вернуть 0 страниц
        assertEquals(0, calculateNextPage(null, 10))
        assertEquals(0, calculateNextPage(0, 10))
    }

    @Test
    fun exactPageSizeReturnsOnePage() {
        // Если элементов ровно на одну страницу, результат — 1
        assertEquals(1, calculateNextPage(10, 10))
    }

    @Test
    fun moreThanOnePageButNotFullSecondPage() {
        // Любое количество элементов, чуть больше полного размера страницы, округляется вверх
        assertEquals(1, calculateNextPage(9, 10))   // 0.9 страниц -> 1
        assertEquals(2, calculateNextPage(11, 10))  // 1.1 страниц -> 2
        assertEquals(2, calculateNextPage(14, 10))  // 1.4 страниц -> 2
        assertEquals(2, calculateNextPage(15, 10))  // 1.5 страниц -> 2
        assertEquals(2, calculateNextPage(19, 10))  // 1.9 страниц -> 2
    }

    @Test
    fun pageSizeLessOrEqualZeroReturnsZero() {
        // Если размер страницы нулевой или отрицательный — всегда 0 страниц
        assertEquals(0, calculateNextPage(10, 0))
        assertEquals(0, calculateNextPage(10, -1))
    }

    @Test
    fun listSmallerThanPageSizeReturnsOnePage() {
        // Если элементов меньше размера страницы, всё равно одна страница
        assertEquals(1, calculateNextPage(1, 10))
        assertEquals(1, calculateNextPage(7, 10))
    }
}
