package org.example.library.di.modules

import org.example.library.appRouter.AppRouter
import org.koin.core.Koin
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val appRouterModule: Module = module {
    singleOf(::AppRouter)
}

fun Koin.getAppRouter(): AppRouter {
    return get()
}
