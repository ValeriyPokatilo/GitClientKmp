package org.example.library.di

import dev.icerock.moko.errors.mappers.ExceptionMappersStorage
import dev.icerock.moko.network.errors.NetworkErrorsTexts
import dev.icerock.moko.network.errors.registerAllNetworkMappers
import dev.icerock.moko.network.exceptions.ErrorException
import dev.icerock.moko.network.exceptions.ValidationException
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import io.github.aakira.napier.Napier
import org.example.library.AppRes

internal fun configureExceptionMappers() {
    ExceptionMappersStorage
        .registerAllNetworkMappers(errorsTexts = NetworkErrorsTexts())
        .register<ErrorException, StringDesc> {
            it.description?.desc() ?: AppRes.strings.unknown_error.desc()
        }
        .register<ValidationException, StringDesc> {
            it.errors.firstOrNull()?.message?.desc() ?: AppRes.strings.unknown_error.desc()
        }
        .onEach { e, _, _ ->
            Napier.e("Exception mapped", e)
        }
}
