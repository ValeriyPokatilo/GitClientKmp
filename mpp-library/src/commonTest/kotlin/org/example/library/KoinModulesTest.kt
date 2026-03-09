package org.example.library

import dev.icerock.moko.test.robolectric.RobolectricTestCases
import org.example.library.di.registerKoinModules
import org.example.sample.ExampleViewModel
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.test.check.ParametersBinding
import org.koin.test.check.checkModules
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Ignore
import kotlin.test.Test

expect val platformModule: Module
expect fun ParametersBinding.platformBindings()

class KoinModulesTest : RobolectricTestCases() {
    override val rules: List<Rule>
        get() = emptyList()

    private lateinit var koinApp: KoinApplication

    @BeforeTest
    fun setUp() {
        koinApp = startKoin {
            modules(platformModule)
            modules(registerKoinModules("https://localhost"))
        }
    }

    @Ignore
    @Test
    fun verifyModules() {
        koinApp.checkModules {
            platformBindings()

            withInstance(ExampleViewModel.Params(argument = "TestArg"))
        }
    }

    @AfterTest
    fun teardown() {
        stopKoin()
    }
}
