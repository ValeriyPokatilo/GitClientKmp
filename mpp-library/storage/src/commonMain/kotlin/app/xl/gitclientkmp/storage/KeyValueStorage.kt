package app.xl.gitclientkmp.storage

import app.xl.gitclientkmp.logger.Logger
import com.russhwolf.settings.Settings

class KeyValueStorage(
    private val settings: Settings
) {

    companion object {
        private const val KEY_AUTH_TOKEN = "auth_token"
    }

    fun saveToken(token: String) {
        Logger.info(message = "KeyValueStorage: save token")
        settings.putString(key = KEY_AUTH_TOKEN, value = token)
    }

    fun getToken(): String? {
        val token = settings.getStringOrNull(key = KEY_AUTH_TOKEN)
        Logger.info(message = "KeyValueStorage: get token - $token")
        return token
    }

    fun clearToken() {
        Logger.info(message = "KeyValueStorage: clear token")
        settings.remove(key = KEY_AUTH_TOKEN)
    }
}
