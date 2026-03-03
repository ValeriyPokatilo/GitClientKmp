package org.example.library

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.check.ParametersBinding

actual fun ParametersBinding.platformBindings() {
    // nop
}

actual val platformModule: Module = module {
    val app: Application = ApplicationProvider.getApplicationContext()
    single<Context> { app }
    single<Application> { app }
}
