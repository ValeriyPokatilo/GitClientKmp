package app.xl.gitclientkmp.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageUploadResponse(
    @SerialName("data") val data: Data
) {
    @Serializable
    data class Data(
        @SerialName("display_url") val displayUrl: String
    )
}
