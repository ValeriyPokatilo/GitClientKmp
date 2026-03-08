package app.xl.androidapp.app.xl.gitclientkmp.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReadmeDto(
    val encoding: String,
    val content: String
)