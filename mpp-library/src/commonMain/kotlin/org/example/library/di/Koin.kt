package org.example.library.di

import app.xl.gitclientkmp.di.authModule
import app.xl.gitclientkmp.di.commonModule
import app.xl.gitclientkmp.di.repoModule
import dev.icerock.moko.crashreporting.core.ExceptionLogger
import dev.icerock.moko.crashreporting.napier.CrashReportingAntilog
import io.github.aakira.napier.Antilog
import io.github.aakira.napier.Napier
import org.example.library.di.modules.apiModule
import org.example.library.di.modules.appRouterModule
import org.example.library.di.modules.featuresModules
import org.example.library.di.modules.keyValueStorageModule
import org.example.library.di.modules.networkModule
import org.example.library.di.modules.platformModule
import org.example.library.di.modules.repositoriesModule
import org.example.library.di.modules.useCasesModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

fun startDI(
    baseUrl: String,
    antilog: Antilog?,
    exceptionLogger: ExceptionLogger,
    appDeclaration: KoinAppDeclaration? = null,
): KoinApplication {
    antilog?.also { Napier.base(antilog = it) }
    Napier.base(CrashReportingAntilog(exceptionLogger))
    configureExceptionMappers()

    return startKoin {
        modules(registerKoinModules(baseUrl))
        appDeclaration?.invoke(this)
    }
}

internal fun registerKoinModules(
    baseUrl: String,
): List<Module> = listOf(
    platformModule,
    networkModule,
    apiModule(baseUrl = baseUrl),
    keyValueStorageModule,
    featuresModules,
    repositoriesModule,
    useCasesModule,
    appRouterModule,
    authModule,
    repoModule,
    commonModule

//    Uncomment for provide exceptionHandler
//    exceptionHandlerModule
)
