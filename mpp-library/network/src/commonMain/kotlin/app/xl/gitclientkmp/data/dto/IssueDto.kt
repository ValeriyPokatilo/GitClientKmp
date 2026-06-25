package app.xl.gitclientkmp.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IssueDto(
    @SerialName("id") val id: Long,
    @SerialName("title") val title: String,
    @SerialName("state") val state: String,
    @SerialName("number") val number: Int,
    @SerialName("body") val body: String,
    @SerialName("updated_at") val updatedAt: String
)
