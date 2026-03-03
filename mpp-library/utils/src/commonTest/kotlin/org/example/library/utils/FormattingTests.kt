package org.example.library.utils

import org.example.library.utils.formatPhone
import kotlin.test.Test
import kotlin.test.assertEquals

class FormattingTests {
    @Test
    fun `test phone format`() {
        assertEquals(
            expected = "+7 (999) 888-77-66",
            actual = "79998887766".formatPhone()
        )
        assertEquals(
            expected = "+6 (987) 654-32-10",
            actual = "69876543210".formatPhone()
        )
    }
}
