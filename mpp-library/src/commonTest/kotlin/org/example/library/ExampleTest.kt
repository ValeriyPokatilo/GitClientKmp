package org.example.library

import org.example.library.di.runConfiguredTest
import org.example.library.model.AuthTokens
import org.example.library.storage.KeyValueStorage
import org.example.sample.ExampleViewModel
import org.koin.core.parameter.parametersOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ExampleTest {

    @Test
    fun `test key value storage`() = runConfiguredTest { koin ->
        val keyValueStorage: KeyValueStorage = koin.get()

        assertNull(keyValueStorage.tokens?.accessToken)
        keyValueStorage.tokens = AuthTokens("hello", "")
        assertEquals(expected = "hello", actual = keyValueStorage.tokens?.accessToken)
    }

    @Test
    fun `test example view model`() = runConfiguredTest { koin ->
        val viewModel: ExampleViewModel = koin.get {
            parametersOf(
                ExampleViewModel.Params(
                    argument = "testArg"
                )
            )
        }
        val keyValueStorage: KeyValueStorage = koin.get()

        assertEquals(expected = "", actual = viewModel.language.value)
        assertNull(keyValueStorage.language)

        viewModel.language.value = "hello"
        assertNull(keyValueStorage.language)

        assertEquals(expected = "testArg", actual = viewModel.getArgValue())

        viewModel.saveAction()
        assertEquals(expected = "hello", actual = keyValueStorage.language)
    }
}
