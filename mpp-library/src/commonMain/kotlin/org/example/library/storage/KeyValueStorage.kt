package org.example.library.storage

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.Settings
import com.russhwolf.settings.nullableString
import com.russhwolf.settings.serialization.nullableSerializedValue
import kotlinx.serialization.ExperimentalSerializationApi
import org.example.library.model.AuthTokens
import org.example.library.model.TokenStorage

@OptIn(ExperimentalSerializationApi::class, ExperimentalSettingsApi::class)
internal class KeyValueStorage(
    private val settings: Settings
) : TokenStorage {
    override var tokens: AuthTokens? by settings.nullableSerializedValue(
        serializer = AuthTokens.serializer(),
        key = "pref_tokens"
    )

    var language: String? by settings.nullableString("pref_language")

    fun clear() {
        settings.clear()
    }
}
