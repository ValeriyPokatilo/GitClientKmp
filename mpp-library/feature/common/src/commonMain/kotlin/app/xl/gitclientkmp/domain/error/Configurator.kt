package app.xl.gitclientkmp.domain.error

import app.xl.gitclientkmp.MR
import app.xl.gitclientkmp.domain.entity.AppError
import dev.icerock.moko.errors.mappers.ExceptionMappersStorage
import dev.icerock.moko.errors.mappers.mapThrowable
import dev.icerock.moko.resources.desc.Raw
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc

object Configurator {
    fun init() {
        ExceptionMappersStorage
            .register<AppError.Http, StringDesc> { exc ->
                exc.errorMessage?.let { StringDesc.Raw(it) }
                    ?: MR.strings.repositories_connection_error_message.desc()
            }
            .register<AppError.Network, StringDesc> {
                MR.strings.repositories_connection_error_message.desc()
            }

            .condition<ErrorModel>({ it is AppError.Http }) { exc ->
                val http = exc as AppError.Http

                ErrorModel(
                    title = StringDesc.Raw(http.code.toString()),
                    message = exc.mapThrowable(),
                    icon = ErrorIcon.Http
                )
            }

            .condition<ErrorModel>({ it is AppError.Network }) {
                ErrorModel(
                    title = MR.strings.repositories_connection_error_title.desc(),
                    message = MR.strings.repositories_connection_error_message.desc(),
                    icon = ErrorIcon.Network
                )
            }

            .condition<ErrorModel>({ true }) {
                ErrorModel(
                    title = MR.strings.repositories_connection_error_title.desc(),
                    message = MR.strings.repositories_connection_error_message.desc(),
                    icon = ErrorIcon.Network
                )
            }

            .setFallbackValue(
                ErrorModel(
                    title = MR.strings.repositories_connection_error_title.desc(),
                    message = MR.strings.repositories_connection_error_message.desc(),
                    icon = ErrorIcon.Network
                )
            )
    }
}