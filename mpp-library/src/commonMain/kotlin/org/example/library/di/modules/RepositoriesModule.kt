package org.example.library.di.modules

import org.example.library.repositories.domain.AuthDomainRepository
import org.example.library.repositories.impl.ExampleRepositoryImpl
import org.example.sample.model.ExampleRepository
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val repositoriesModule: Module = module {
    // domain
    singleOf(::AuthDomainRepository)

    // implementations
    singleOf(::ExampleRepositoryImpl) bind ExampleRepository::class
}
