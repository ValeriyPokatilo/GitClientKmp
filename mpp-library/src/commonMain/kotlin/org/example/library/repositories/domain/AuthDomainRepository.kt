package org.example.library.repositories.domain


internal class AuthDomainRepository(
    private val keyValueStorage: KeyValueStorage,
) {

    fun isAuthorized(): Boolean {
        return keyValueStorage.getToken() != null
    }
}
