package org.example.library.usecase

import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.plugin
import org.example.library.model.AuthTokens
import org.example.library.model.TokenStorage
import org.example.library.storage.KeyValueStorage

/**
 * Use-case для обновления токенов аутентификации и сброса кэшированных токенов в [HttpClient].
 *
 * Выполняет две задачи:
 * 1. Обновляет токены в [TokenStorage], который реализован в [KeyValueStorage].
 * 2. Сбрасывает закэшированные токены в [HttpClient], чтобы при следующем запросе они перечитались
 *    заново из хранилища через `loadTokens` внутри [BearerAuthProvider].
 *
 * ### Применение:
 * После успешной авторизации:
 *
 * ```
 * val tokens = AuthTokens(
 *     accessToken = "abc123",
 *     refreshToken = "refresh456"
 * )
 * updateAuthTokensUseCase(tokens)
 * ```
 *
 * При разлогине:
 *
 * ```
 * updateAuthTokensUseCase(null)
 * ```
 *
 * Это важно, так как [HttpClient] сохраняет accessToken в памяти после первого успешного использования,
 * и не будет заново запрашивать его из [TokenStorage], пока не будет вызван
 * [BearerAuthProvider.clearToken] или создан новый клиент.
 *
 * @param tokenStorage Локальное хранилище токенов.
 * @param httpClient HTTP-клиент с установленным плагином [Auth] и [BearerAuthProvider].
 */
class UpdateAuthTokensUC(
    private val tokenStorage: TokenStorage,
    private val httpClient: HttpClient
) {
    /**
     * Обновляет текущие токены в хранилище и сбрасывает кэш токенов в [HttpClient].
     *
     * @param tokens Новые токены или `null`, если необходимо удалить текущие токены.
     */
    operator fun invoke(tokens: AuthTokens?) {
        tokenStorage.tokens = tokens

        // чистим данные токенов в HttpClient'е, чтобы перечитались с хранилища
        httpClient.plugin(Auth).providers
            .filterIsInstance<BearerAuthProvider>()
            .forEach { it.clearToken() }
    }
}
