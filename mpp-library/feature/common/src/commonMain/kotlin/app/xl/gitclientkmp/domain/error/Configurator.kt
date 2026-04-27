package app.xl.gitclientkmp.domain.error

import app.xl.gitclientkmp.MR
import app.xl.gitclientkmp.domain.entity.AppError
import dev.icerock.moko.errors.mappers.ExceptionMappersStorage
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
            .register<AppError.Unauthorized, StringDesc> {
                MR.strings.invalid_token_reason.desc()
            }
            .condition<ErrorModel>(condition = { it is AppError.Http }) { exc ->
                val http: AppError.Http = exc as AppError.Http

                val errorCode: String = http.code.toString()
                val errorMessage: String = http.message.toString()

                ErrorModel(
                    title = StringDesc.Raw(string = "$errorMessage / $errorCode"),
                    message = MR.strings.info_for_developer.desc(),
                    isNetworkError = false
                )
            }
            .condition<ErrorModel>(condition = { it is AppError.Network }) {
                ErrorModel(
                    title = MR.strings.repositories_connection_error_title.desc(),
                    message = MR.strings.repositories_connection_error_message.desc(),
                    isNetworkError = true
                )
            }
            .condition<ErrorModel>(condition = { it is AppError.Unauthorized }) {
                ErrorModel(
                    title = MR.strings.invalid_token_reason.desc(),
                    message = MR.strings.info_for_developer.desc(),
                    isNetworkError = false
                )
            }
            .condition<ErrorModel>(condition = { true }) {
                ErrorModel(
                    title = MR.strings.repositories_connection_error_title.desc(),
                    message = MR.strings.repositories_connection_error_message.desc(),
                    isNetworkError = true
                )
            }
            .setFallbackValue(
                value = ErrorModel(
                    title = MR.strings.repositories_connection_error_title.desc(),
                    message = MR.strings.repositories_connection_error_message.desc(),
                    isNetworkError = true
                )
            )
    }
}
