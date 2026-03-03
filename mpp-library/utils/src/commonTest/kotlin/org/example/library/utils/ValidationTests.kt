package org.example.library.utils

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ValidationTests {
    @Test
    fun `test valid emails`() {
        listOf(
            "am@icerock.dev",
            "aleksey.mikhailov@icerock.dev",
            "kmm-dev@icerock.dev",
            "am+1@icerock.dev",
            "a@am.ru"
        ).forEach { email ->
            assertTrue(
                actual = Validations.EMAIL_REGEX.matches(email),
                message = "valid email $email can't pass validation"
            )
        }
    }

    @Test
    fun `test invalid emails`() {
        listOf(
            "@icerock.dev",
            "am@icerock",
            "am@",
            "@",
            "am@am@icerock.dev"
        ).forEach { email ->
            assertFalse(
                actual = Validations.EMAIL_REGEX.matches(email),
                message = "invalid email $email pass validation"
            )
        }
    }

    @Test
    fun `test valid passwords`() {
        listOf(
            "123456789asbs",
            "123456789012345678901234567890123456789012345678a",
            "asd-123s%asd#dsa$"
        ).forEach { password ->
            assertTrue(
                actual = Validations.PASSWORD_REGEX.matches(password),
                message = "invalid password $password can't pass validation"
            )
        }
    }

    @Test
    fun `test invalid passwords`() {
        listOf(
            "123456789",
            "123",
            "12345678901234567890123456789012345678901234567890",
            "absdrewgsd",
            "$#@!@#*(#$@"
        ).forEach { password ->
            assertFalse(
                actual = Validations.PASSWORD_REGEX.matches(password),
                message = "invalid password $password pass validation"
            )
        }
    }
}
