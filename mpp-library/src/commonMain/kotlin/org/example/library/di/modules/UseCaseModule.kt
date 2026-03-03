package org.example.library.di.modules

import org.example.library.usecase.UpdateAuthTokensUC
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val useCasesModule: Module = module {
    singleOf(::UpdateAuthTokensUC)
}
