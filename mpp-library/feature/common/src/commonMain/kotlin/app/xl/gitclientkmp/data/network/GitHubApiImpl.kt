package app.xl.gitclientkmp.data.network

import app.xl.gitclientkmp.data.dto.UserInfoDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header

class GitHubApiImpl(
    private val client: HttpClient
) : GitHubApi {

    override suspend fun getUser(token: String): UserInfoDto {
        return client.get("https://api.github.com/user") {
            header("Authorization", token)
        }.body()
    }
}
