package app.xl.gitclientkmp.data.repository

import app.xl.gitclientkmp.data.dto.GitHubErrorDto
import app.xl.gitclientkmp.data.network.GitHubApi
import app.xl.gitclientkmp.data.network.toBearerHeader
import app.xl.gitclientkmp.data.repository.mappers.toEntity
import app.xl.gitclientkmp.data.storage.KeyValueStorage
import app.xl.gitclientkmp.domain.AppRepository
import app.xl.gitclientkmp.domain.entity.AppError
import app.xl.gitclientkmp.domain.entity.UserInfo
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

class AppRepositoryImpl(
    private val api: GitHubApi,
    private val json: Json,
    private val keyValueStorage: KeyValueStorage
) : AppRepository {

    @Throws(Exception::class)
    override suspend fun signIn(token: String): UserInfo {

        val authHeader = token.toBearerHeader()

        try {
            keyValueStorage.authToken = token
            return api.getUser(authHeader).toEntity()
        } catch (e: ResponseException) {
            val body = runCatching {
                e.response.bodyAsText()
            }.getOrNull()

            val message = runCatching {
                body?.let { json.decodeFromString<GitHubErrorDto>(it).message }
            }.getOrNull()

            throw AppError.Http(
                code = e.response.status.value,
                errorMessage = message,
                cause = e
            )
        } catch (e: Exception) {
            throw AppError.Network(e)
        }
    }
}
