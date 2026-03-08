package app.xl.gitclientkmp.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class LicenseDto(
    val name: String,
    val url: String? = null
)
