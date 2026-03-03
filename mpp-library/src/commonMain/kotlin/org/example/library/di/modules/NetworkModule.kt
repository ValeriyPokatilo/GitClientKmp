package org.example.library.di.modules

import dev.icerock.moko.network.createHttpClientEngine
import dev.icerock.moko.network.exceptionfactory.HttpExceptionFactory
import dev.icerock.moko.network.exceptionfactory.parser.ErrorExceptionParser
import dev.icerock.moko.network.exceptionfactory.parser.ValidationExceptionParser
import dev.icerock.moko.network.generated.apis.AuthApi
import dev.icerock.moko.network.plugins.ExceptionPlugin
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.Json
import org.example.library.ChannelLogoutHandler
import org.example.library.model.AuthTokens
import org.example.library.model.TokenStorage
import org.example.library.model.toBearerTokens
import org.example.library.utils.logout.LogoutHandler
import org.koin.core.Koin
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val networkModule: Module = module {
    single<Json> {
        Json {
            ignoreUnknownKeys = true
        }
    }
    single<Logger> {
        object : Logger {
            override fun log(message: String) {
                Napier.d(message = message, tag = "KTOR")
            }
        }
    }

    singleOf(::createHttpClient)

    singleOf<HttpClientEngine>({ createHttpClientEngine() })
    singleOf(::ChannelLogoutHandler) bind LogoutHandler::class
}

fun Koin.getLogoutHandler(): LogoutHandler {
    return get()
}

/**
 * Создает базовый HttpClient без обработки ошибок, авторизации и прочего.
 * Нужен чтобы запросы обновления токенов кидать им и не получать ошибки.
 */
private fun createBasicHttpClient(
    httpClientEngine: HttpClientEngine,
    requestLogger: Logger,
): HttpClient {
    return HttpClient(httpClientEngine) {
        install(Logging) {
            logger = requestLogger
            level = LogLevel.INFO
        }
    }
}

@Suppress("UnusedPrivateProperty")
private fun createHttpClient(
    json: Json,
    tokenStorage: TokenStorage,
    requestLogger: Logger,
    httpClientEngine: HttpClientEngine,
    authApiFactory: AuthApiFactory,
    logoutHandler: LogoutHandler,
): HttpClient {
    val basicHttpClient: HttpClient = createBasicHttpClient(
        httpClientEngine = httpClientEngine,
        requestLogger = requestLogger
    )

    return basicHttpClient.config {
        install(ExceptionPlugin) {
            exceptionFactory = HttpExceptionFactory(
                defaultParser = ErrorExceptionParser(json),
                customParsers = mapOf(
                    HttpStatusCode.UnprocessableEntity.value to ValidationExceptionParser(json)
                )
            )
        }

        install(Auth) {
            bearer {
                // для каких запросов сразу же применяем наш имеющийся токен
                // для всех запросов нашего http клиент пробуем подставлять токен
                // так как через наш httpClient мы ходим только в нашу собственную API.
                sendWithoutRequest { true }
                // загрузка токенов из хранилища для отправки в запросах
                loadTokens {
                    tokenStorage.tokens?.toBearerTokens()
                }
                // обновление при ошибке
                refreshTokens {
                    // если нет токенов - мы не можем рефрешить
                    val refreshToken: String = this.oldTokens?.refreshToken
                        ?: return@refreshTokens null

                    // для проведения авторизации берем другой http клиент
                    // у которого не производится подстановка токена чтобы не зависнуть
                    val authApi: AuthApi = authApiFactory.create(basicHttpClient)

                    try {
                        Napier.d("try to refresh tokens")
                        //TODO: Update logic on real request
//                        val response: SuccessResponse = authApi.refreshToken(
//                            token = "key"
//                        )
                        Napier.d("refresh response received")

                        //TODO: Update logic on real data from response
                        val tokens: AuthTokens = AuthTokens(
                            accessToken = "token",
                            refreshToken = "refreshToken"
                        )

                        val authTokens = AuthTokens(
                            accessToken = tokens.accessToken,
                            refreshToken = tokens.refreshToken
                        )
                        tokenStorage.tokens = authTokens

                        Napier.d("tokens saved")

                        authTokens.toBearerTokens()
                    } catch (exc: Exception) {
                        // не удалось продлить
                        Napier.e("can't refresh tokens", exc)
                        logoutHandler.onLogout()

                        null
                    }
                }
            }
        }

        // disable standard BadResponseStatus - exceptionfactory do it for us
        expectSuccess = false
    }
}
