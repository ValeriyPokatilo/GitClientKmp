package app.xl.gitclientkmp.data.repository

import app.xl.gitclientkmp.AppError
import app.xl.gitclientkmp.Repository
import app.xl.gitclientkmp.RepositoryDetails
import app.xl.gitclientkmp.UserInfo
import app.xl.gitclientkmp.data.dto.GitHubErrorDto
import app.xl.gitclientkmp.data.dto.ReadmeDto
import app.xl.gitclientkmp.data.network.GitHubApi
import app.xl.gitclientkmp.data.repository.mappers.toEntity
import app.xl.gitclientkmp.data.storage.KeyValueStorage
import app.xl.gitclientkmp.data.utils.Base64Decoder
import app.xl.gitclientkmp.data.utils.Logger
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
        Logger.info(message = "AppRepositoryImpl: sign in started")
        try {
            val user: UserInfo = api.getUser(token = token).toEntity()
            Logger.info(message = "AppRepositoryImpl: sign in success - ${user.login}")
            keyValueStorage.saveToken(token = token)
            return user
        } catch (exception: Throwable) {
            Logger.info(message = "AppRepositoryImpl: sign in failed - $exception")
            mapException(exception = exception)
        }
    }

    @Throws(Exception::class)
    override suspend fun getRepositories(): List<Repository> {
        Logger.info(message = "AppRepositoryImpl: get repositories started")
        try {
            val result = api.getRepositories()
            Logger.info(
                message = "AppRepositoryImpl: get repositories success - ${result.size} items"
            )
            return result.map { it.toEntity() }
        } catch (exception: Throwable) {
            Logger.info(message = "AppRepositoryImpl: get repositories failed - $exception")
            mapException(exception = exception)
        }
    }

    @Throws(Exception::class)
    override suspend fun getRepository(
        ownerName: String,
        repositoryName: String
    ): RepositoryDetails {
        Logger.info(message = "AppRepositoryImpl: get repository started")
        try {
            val result = api.getRepository(
                ownerName = ownerName,
                repositoryName = repositoryName
            )
            Logger.info(message = "AppRepositoryImpl: get repository success")
            return result.toEntity()
        } catch (exception: Throwable) {
            Logger.info(message = "AppRepositoryImpl: get repository failed - $exception")
            mapException(exception = exception)
        }
    }

    @Throws(Exception::class)
    override suspend fun getRepositoryReadme(
        ownerName: String,
        repositoryName: String,
        branchName: String?
    ): String {
        Logger.info(message = "AppRepositoryImpl: get repository readme started")
        return try {
            val readmeDto: ReadmeDto = api.getRepositoryReadme(
                ownerName = ownerName,
                repositoryName = repositoryName,
                branchName = branchName
            )

            if (readmeDto.encoding != BASE64_ENCODING) {
                Logger.info(
                    message = "AppRepositoryImpl: get repository readme success, but not decoded"
                )
                ""
            } else {
                val decodedBytes: ByteArray = Base64Decoder.decode(encoded = readmeDto.content)
                Logger.info(
                    message = "AppRepositoryImpl: get repository readme success - $decodedBytes"
                )
                decodedBytes.decodeToString()
            }
        } catch (exception: ResponseException) {
            Logger.info(message = "AppRepositoryImpl: get repository readme failed - $exception")
            if (exception.response.status.value == NOT_FOUND) {
                ""
            } else {
                mapException(exception = exception)
            }
        } catch (exception: Throwable) {
            Logger.info(message = "AppRepositoryImpl: get repository readme failed - $exception")
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
        Logger.info(message = "AppRepositoryImpl: logout")
        keyValueStorage.clearToken()
    }

    companion object {
        private const val NOT_FOUND = 404
        private const val BASE64_ENCODING = "base64"
    }
}
