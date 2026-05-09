package app.xl.gitclientkmp.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateIssueRequestDto(
    @SerialName("title") val title: String,
    @SerialName("body") val body: String
)
