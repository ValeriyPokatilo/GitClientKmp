package org.example.library.model

import io.ktor.client.plugins.auth.providers.BearerTokens
import kotlinx.serialization.Serializable

@Serializable
data class AuthTokens(
    val accessToken: String,
    val refreshToken: String
)

internal fun AuthTokens.toBearerTokens(): BearerTokens = BearerTokens(
    accessToken = accessToken,
    refreshToken = refreshToken
)
