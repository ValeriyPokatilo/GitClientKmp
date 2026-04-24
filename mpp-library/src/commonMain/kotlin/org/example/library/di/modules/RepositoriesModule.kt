package org.example.library.di.modules

import org.example.library.repositories.domain.AuthDomainRepository
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val repositoriesModule: Module = module {
    // domain
    singleOf(::AuthDomainRepository)
}
