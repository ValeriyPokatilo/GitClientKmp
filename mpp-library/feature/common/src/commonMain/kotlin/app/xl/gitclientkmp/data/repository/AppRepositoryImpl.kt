package app.xl.gitclientkmp.data.repository

import app.xl.gitclientkmp.data.dto.GitHubErrorDto
import app.xl.gitclientkmp.data.dto.ReadmeDto
import app.xl.gitclientkmp.data.network.GitHubApi
import app.xl.gitclientkmp.data.repository.mappers.toEntity
import app.xl.gitclientkmp.data.storage.KeyValueStorage
import app.xl.gitclientkmp.data.utils.Base64Decoder
import app.xl.gitclientkmp.domain.entity.AppError
import app.xl.gitclientkmp.domain.entity.Repository
import app.xl.gitclientkmp.domain.entity.RepositoryDetails
import app.xl.gitclientkmp.domain.entity.UserInfo
import app.xl.gitclientkmp.domain.repository.AppRepository
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

@Suppress("TooGenericExceptionCaught")
class AppRepositoryImpl(
    private val api: GitHubApi,
    private val json: Json,
    private val keyValueStorage: KeyValueStorage
) : AppRepository {

    @Throws(Exception::class)
    override suspend fun signIn(token: String): UserInfo {
        try {
            val user: UserInfo = api.getUser(token = token).toEntity()
            keyValueStorage.saveToken(token = token)
            return user
        } catch (exception: Throwable) {
            mapException(exception = exception)
        }
    }

    @Throws(Exception::class)
    override suspend fun getRepositories(): List<Repository> {
        try {
            return api
                .getRepositories()
                .map { it.toEntity() }
        } catch (exception: Throwable) {
            mapException(exception = exception)
        }
    }

    @Throws(Exception::class)
    override suspend fun getRepository(
        ownerName: String,
        repositoryName: String
    ): RepositoryDetails {
        try {
            return api
                .getRepository(
                    ownerName = ownerName,
                    repositoryName = repositoryName
                )
                .toEntity()
        } catch (exception: Throwable) {
            mapException(exception = exception)
        }
    }

    @Throws(Exception::class)
    override suspend fun getRepositoryReadme(
        ownerName: String,
        repositoryName: String,
        branchName: String?
    ): String {
        return try {
            val readmeDto: ReadmeDto = api.getRepositoryReadme(
                ownerName = ownerName,
                repositoryName = repositoryName,
                branchName = branchName
            )

            if (readmeDto.encoding != BASE64_ENCODING) {
                ""
            } else {
                val decodedBytes: ByteArray = Base64Decoder.decode(encoded = readmeDto.content)
                decodedBytes.decodeToString()
            }
        } catch (exception: ResponseException) {
            if (exception.response.status.value == NOT_FOUND) {
                ""
            } else {
                mapException(exception = exception)
            }
        } catch (exception: Throwable) {
            mapException(exception = exception)
        }
    }

    private suspend fun mapException(exception: Throwable): Nothing {
        when (exception) {
            is ResponseException -> {
                val body: String? = runCatching { exception.response.bodyAsText() }.getOrNull()

                val message: String? = runCatching {
                    body?.let { json.decodeFromString<GitHubErrorDto>(string = it).message }
                }.getOrNull()

                val code: Int = exception.response.status.value

                throw AppError.Http(
                    code = code,
                    errorMessage = message,
                    cause = exception
                )
            }

            else -> throw AppError.Network(cause = exception)
        }
    }

    override fun logout() {
        keyValueStorage.clearToken()
    }

    companion object {
        private const val NOT_FOUND = 404
        private const val BASE64_ENCODING = "base64"
    }
}
