package org.example.library.utils

import org.example.library.utils.formatMinutesSeconds
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class DurationTests {
    @Test
    fun `test duration format`() {
        assertEquals(
            expected = "01:02",
            (1.minutes + 2.seconds).formatMinutesSeconds()
        )
        assertEquals(
            expected = "01:00",
            (1.minutes).formatMinutesSeconds()
        )
        assertEquals(
            expected = "00:02",
            (2.seconds).formatMinutesSeconds()
        )
        assertEquals(
            expected = "",
            (0.seconds).formatMinutesSeconds()
        )
    }
}
