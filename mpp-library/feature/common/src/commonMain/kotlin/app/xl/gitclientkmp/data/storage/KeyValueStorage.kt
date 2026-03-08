package app.xl.gitclientkmp.data.storage

import com.russhwolf.settings.Settings

class KeyValueStorage(
    private val settings: Settings
) {

    var authToken: String?
        get() = settings.getStringOrNull("auth_token")
        set(value) {
            if (value == null) {
                settings.remove("auth_token")
            } else {
                settings.putString("auth_token", value)
            }
        }
}
