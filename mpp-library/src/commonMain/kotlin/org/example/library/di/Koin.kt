package org.example.library.di

import app.xl.gitclientkmp.di.authModule
import app.xl.gitclientkmp.di.networkModule
import app.xl.gitclientkmp.di.repoModule
import dev.icerock.moko.crashreporting.core.ExceptionLogger
import dev.icerock.moko.crashreporting.napier.CrashReportingAntilog
import io.github.aakira.napier.Antilog
import io.github.aakira.napier.Napier
import org.example.library.di.modules.appRouterModule
import org.example.library.di.modules.repositoriesModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

fun startDI(
    antilog: Antilog?,
    exceptionLogger: ExceptionLogger,
    appDeclaration: KoinAppDeclaration? = null,
): KoinApplication {
    antilog?.also { Napier.base(antilog = it) }
    Napier.base(CrashReportingAntilog(exceptionLogger))

    return startKoin {
        modules(registerKoinModules())
        appDeclaration?.invoke(this)
    }
}

internal fun registerKoinModules(): List<Module> = listOf(
    repositoriesModule,
    appRouterModule,
    authModule,
    repoModule,
    networkModule,
    appRouterModule
)
