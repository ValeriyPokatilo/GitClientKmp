package app.xl.gitclientkmp.domain.error

import app.xl.gitclientkmp.AppError
import app.xl.gitclientkmp.MR
import dev.icerock.moko.errors.mappers.ExceptionMappersStorage
import dev.icerock.moko.resources.desc.Raw
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import dev.icerock.moko.resources.format

object Configurator {
    fun init() {
        ExceptionMappersStorage
            .register<AppError.Http, StringDesc> { exc ->
                exc.errorMessage?.let { StringDesc.Raw(it) }
                    ?: MR.strings.connection_error_message.desc()
            }
            .register<AppError.Network, StringDesc> {
                MR.strings.connection_error_message.desc()
            }
            .register<AppError.Unauthorized, StringDesc> {
                MR.strings.invalid_token_reason.desc()
            }
            .condition<ErrorModel>(condition = { it is AppError.Http }) { exc ->
                val http: AppError.Http = exc as AppError.Http

                val errorCode: String = http.code.toString()
                val errorMessage: String = http.message.toString()

                ErrorModel(
                    placeholderTitle = MR.strings.request_error_title.desc(),
                    placeholderMessage = MR.strings.request_error_message.desc(),
                    alertMessage = MR.strings.error_with_dev_info.format(
                        "$errorMessage / $errorCode",
                        MR.strings.info_for_developer.desc()
                    ),
                    isNetworkError = false
                )
            }
            .condition<ErrorModel>(condition = { it is AppError.Network }) {
                ErrorModel(
                    placeholderTitle = MR.strings.connection_error_title.desc(),
                    placeholderMessage = MR.strings.connection_error_message.desc(),
                    isNetworkError = true
                )
            }
            .condition<ErrorModel>(condition = { it is AppError.Unauthorized }) {
                ErrorModel(
                    placeholderTitle = MR.strings.invalid_token_reason.desc(),
                    placeholderMessage = MR.strings.info_for_developer.desc(),
                    isNetworkError = false
                )
            }
            .condition<ErrorModel>(condition = { true }) {
                ErrorModel(
                    placeholderTitle = MR.strings.connection_error_title.desc(),
                    placeholderMessage = MR.strings.connection_error_message.desc(),
                    isNetworkError = true
                )
            }
            .setFallbackValue(
                value = ErrorModel(
                    placeholderTitle = MR.strings.connection_error_title.desc(),
                    placeholderMessage = MR.strings.connection_error_message.desc(),
                    isNetworkError = true
                )
            )
    }
}
