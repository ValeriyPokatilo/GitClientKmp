package app.xl.gitclientkmp.data.storage

import com.russhwolf.settings.Settings

class KeyValueStorage(
    private val settings: Settings
) {

    companion object {
        private const val KEY_AUTH_TOKEN = "auth_token"
    }

    fun saveToken(token: String) {
        settings.putString(KEY_AUTH_TOKEN, token)
    }

    fun getToken(): String? {
        return settings.getStringOrNull(KEY_AUTH_TOKEN)
    }

    fun clearToken() {
        settings.remove(KEY_AUTH_TOKEN)
    }
}
