package app.xl.gitclientkmp.data.repository

import app.xl.gitclientkmp.data.dto.GitHubErrorDto
import app.xl.gitclientkmp.data.network.GitHubApi
import app.xl.gitclientkmp.data.network.toBearerHeader
import app.xl.gitclientkmp.data.repository.mappers.toEntity
import app.xl.gitclientkmp.data.storage.KeyValueStorage
import app.xl.gitclientkmp.data.utils.Base64Decoder
import app.xl.gitclientkmp.domain.AppRepository
import app.xl.gitclientkmp.domain.entity.AppError
import app.xl.gitclientkmp.domain.entity.Repository
import app.xl.gitclientkmp.domain.entity.RepositoryDetails
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
            keyValueStorage.saveToken(token)
            return api.getUser(authHeader).toEntity()
        } catch (exception: ResponseException) {
            val body = runCatching {
                exception.response.bodyAsText()
            }.getOrNull()

            val message = runCatching {
                body?.let { json.decodeFromString<GitHubErrorDto>(it).message }
            }.getOrNull()

            throw AppError.Http(
                code = exception.response.status.value,
                errorMessage = message,
                cause = exception
            )
        } catch (exception: Exception) {
            throw AppError.Network(exception)
        }
    }

    @Throws(Exception::class)
    override suspend fun getRepositories(): List<Repository> {
        val authHeader = createAuthHeader()

        try {
            return api
                .getRepositories(authHeader)
                .map { it.toEntity() }
        } catch (exception: ResponseException) {
            val body = runCatching {
                exception.response.bodyAsText()
            }.getOrNull()

            val message = runCatching {
                body?.let { json.decodeFromString<GitHubErrorDto>(it).message }
            }.getOrNull()

            throw AppError.Http(
                code = exception.response.status.value,
                errorMessage = message,
                cause = exception
            )
        } catch (exception: Exception) {
            throw AppError.Network(exception)
        }
    }

    @Throws(Exception::class)
    override suspend fun getRepository(
        ownerName: String,
        repositoryName: String
    ): RepositoryDetails {
        val authHeader = createAuthHeader()

        try {
            return api
                .getRepository(authHeader, ownerName, repositoryName)
                .toEntity()
        } catch (exception: ResponseException) {

            val body = runCatching {
                exception.response.bodyAsText()
            }.getOrNull()

            val message = runCatching {
                body?.let { json.decodeFromString<GitHubErrorDto>(it).message }
            }.getOrNull()

            throw AppError.Http(
                code = exception.response.status.value,
                errorMessage = message,
                cause = exception
            )
        } catch (exception: Exception) {
            throw AppError.Network(exception)
        }
    }

    @Throws(Exception::class)
    override suspend fun getRepositoryReadme(
        ownerName: String,
        repositoryName: String,
        branchName: String?
    ): String {

        val authHeader = createAuthHeader()

        try {
            val readmeDto =
                api.getRepositoryReadme(authHeader, ownerName, repositoryName, branchName)

            if (readmeDto.encoding != "base64") return ""

            val decodedBytes = Base64Decoder.decode(readmeDto.content)

            return decodedBytes.decodeToString()

        } catch (exception: ResponseException) {
            if (exception.response.status.value == 404) {
                return ""
            }

            val body = runCatching {
                exception.response.bodyAsText()
            }.getOrNull()

            val message = runCatching {
                body?.let { json.decodeFromString<GitHubErrorDto>(it).message }
            }.getOrNull()

            throw AppError.Http(
                code = exception.response.status.value,
                errorMessage = message,
                cause = exception
            )

        } catch (exception: Exception) {
            throw AppError.Network(exception)
        }
    }

    override fun logout() {
        keyValueStorage.clearToken()
    }

    private fun createAuthHeader(): String {
        val token = keyValueStorage.getToken() ?: throw AppError.Network(
            Exception("invalid_token") // TODO: - make error
        )
        return token.toBearerHeader()
    }
}
