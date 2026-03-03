package org.example.library.repositories.domain

import org.example.library.storage.KeyValueStorage

internal class AuthDomainRepository(
    private val keyValueStorage: KeyValueStorage,
) {

    fun isAuthorized(): Boolean {
        return keyValueStorage.tokens != null
    }
}
