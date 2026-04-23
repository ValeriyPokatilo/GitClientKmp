package app.xl.gitclientkmp.data.storage

import com.russhwolf.settings.Settings

class KeyValueStorage(
    private val settings: Settings
) {

    companion object {
        private const val KEY_AUTH_TOKEN = "auth_token"
    }

    fun saveToken(token: String) {
        settings.putString(key = KEY_AUTH_TOKEN, value = token)
    }

    fun getToken(): String? {
        return settings.getStringOrNull(key = KEY_AUTH_TOKEN)
    }

    fun clearToken() {
        settings.remove(key = KEY_AUTH_TOKEN)
    }
}
