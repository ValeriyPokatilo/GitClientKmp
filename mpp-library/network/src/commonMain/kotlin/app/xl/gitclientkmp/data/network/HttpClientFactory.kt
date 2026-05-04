package app.xl.gitclientkmp.data.network

import app.xl.gitclientkmp.storage.KeyValueStorage
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

        install(plugin = ContentNegotiation) {
            json(json)
        }

        defaultRequest {
            url(urlString = "https://api.github.com")
            header(key = "Accept", value = "application/vnd.github+json")
            header(key = "User-Agent", value = "GitClientKMP")

            keyValueStorage.getToken()?.let {
                header(key = HttpHeaders.Authorization, value = "Bearer $it")
            }
        }
    }
}
