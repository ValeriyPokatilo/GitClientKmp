package org.example.library.usecase

import org.example.library.storage.KeyValueStorage

/**
 * [LogoutUserUC] is responsible for clearing all necessary data on logout:
 * - clear auth tokens
 * - clear all data in repositories
 * [LogoutUserUC] should be provided lazily (`Lazy<LogoutUserUC>`) to avoid cycle with HttpClient
 */
internal class LogoutUserUC(
    private val keyValueStorage: KeyValueStorage,
    private val updateAuthTokensUC: UpdateAuthTokensUC,
) {

    operator fun invoke() {
        // clear auth tokens in HttpClient and platform caches
        updateAuthTokensUC(tokens = null)

        // clear data in all repositories
        keyValueStorage.clear()
    }
}
