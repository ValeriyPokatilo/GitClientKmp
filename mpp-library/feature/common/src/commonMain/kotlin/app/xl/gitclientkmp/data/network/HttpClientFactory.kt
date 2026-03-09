package app.xl.gitclientkmp.data.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun createHttpClient(json: Json): HttpClient {
    return HttpClient {

        expectSuccess = true

        install(ContentNegotiation) {
            json(json)
        }

        defaultRequest {
            header("Accept", "application/vnd.github+json")
            header("User-Agent", "GitClientKMP")
        }
    }
}
