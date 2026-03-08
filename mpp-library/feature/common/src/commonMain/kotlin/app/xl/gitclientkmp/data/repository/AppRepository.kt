package app.xl.gitclientkmp.data.repository

import app.xl.gitclientkmp.data.storage.KeyValueStorage

class AppRepository(
    private val keyValueStorage: KeyValueStorage
) {

    fun saveToken(token: String) {
        keyValueStorage.authToken = token
    }

    fun getToken(): String? {
        return keyValueStorage.authToken
    }
}
