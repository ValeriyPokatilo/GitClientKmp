package org.example.library.repositories.domain

import app.xl.gitclientkmp.storage.KeyValueStorage

internal class AuthDomainRepository(
    private val keyValueStorage: KeyValueStorage,
) {

    fun isAuthorized(): Boolean {
        return keyValueStorage.getToken() != null
    }
}
