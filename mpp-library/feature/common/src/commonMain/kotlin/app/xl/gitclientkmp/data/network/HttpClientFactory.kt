package app.xl.gitclientkmp.data.network

import app.xl.gitclientkmp.data.storage.KeyValueStorage
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun createHttpClient(
    json: Json,
    keyValueStorage: KeyValueStorage
): HttpClient {
    return HttpClient {

        expectSuccess = true

        install(ContentNegotiation) {
            json(json)
        }

        defaultRequest {
            url("https://api.github.com")
            header("Accept", "application/vnd.github+json")
            header("User-Agent", "GitClientKMP")

            keyValueStorage.getToken()?.let {
                header(HttpHeaders.Authorization, "Bearer $it")
            }
        }
    }
}
