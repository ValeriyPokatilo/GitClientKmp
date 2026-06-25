package app.xl.gitclientkmp.data.network

import app.xl.gitclientkmp.data.dto.ImageUploadResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import org.example.app.BuildKonfig

class ImageApiImpl(
    private val client: HttpClient
) : ImageApi {

    override suspend fun uploadImage(bytes: ByteArray): String {
        val response = client.submitFormWithBinaryData(
            url = "https://api.imgbb.com/1/upload",
            formData = formData {
                append(key = "key", value = BuildKonfig.IMGBB_API_KEY)
                append(
                    key = "image",
                    value = bytes,
                    headers = Headers.build {
                        append(name = HttpHeaders.ContentType, value = "image/jpeg")
                        append(
                            name = HttpHeaders.ContentDisposition,
                            value = "filename=\"image.jpg\""
                        )
                    }
                )
            }
        )

        return response.body<ImageUploadResponse>().data.displayUrl
    }
}
