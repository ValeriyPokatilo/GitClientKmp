package org.example.library.di.modules

import dev.icerock.moko.errors.handler.ExceptionHandler
import dev.icerock.moko.errors.mappers.ExceptionMappersStorage
import dev.icerock.moko.errors.presenters.SnackBarDuration.LONG
import dev.icerock.moko.errors.presenters.SnackBarErrorPresenter
import io.github.aakira.napier.Napier
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val exceptionHandlerModule = module {
    singleOf(::createExceptionHandler)
}

internal fun createExceptionHandler(): ExceptionHandler = ExceptionHandler(
    exceptionMapper = ExceptionMappersStorage.throwableMapper(),
    errorPresenter = SnackBarErrorPresenter(duration = LONG),
    onCatch = { error ->
        Napier.e(message = "error caught", throwable = error)
    }
)
