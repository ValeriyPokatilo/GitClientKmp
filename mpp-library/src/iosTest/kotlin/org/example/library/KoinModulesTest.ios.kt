package org.example.library

import org.koin.core.module.Module
import org.koin.dsl.module

actual fun org.koin.test.check.ParametersBinding.platformBindings() {
    // nop
}

actual val platformModule: Module = module {
    // nop
}
