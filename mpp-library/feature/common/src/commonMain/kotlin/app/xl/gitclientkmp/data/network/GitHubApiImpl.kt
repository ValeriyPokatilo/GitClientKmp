package app.xl.gitclientkmp.data.network

import app.xl.gitclientkmp.data.dto.ReadmeDto
import app.xl.gitclientkmp.data.dto.RepoDetailsDto
import app.xl.gitclientkmp.data.dto.RepoDto
import app.xl.gitclientkmp.data.dto.UserInfoDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter

class GitHubApiImpl(
    private val client: HttpClient
) : GitHubApi {

    override suspend fun getUser(token: String): UserInfoDto {
        return client.get("https://api.github.com/user") {
            header("Authorization", token)
        }.body()
    }

    override suspend fun getRepositories(header: String): List<RepoDto> {
        return client.get("https://api.github.com/user/repos") {
            header("Authorization", header)
        }.body()
    }

    override suspend fun getRepository(
        header: String,
        ownerName: String,
        repositoryName: String
    ): RepoDetailsDto {
        return client.get("https://api.github.com/repos/$ownerName/$repositoryName") {
            header("Authorization", header)
        }.body()
    }

    override suspend fun getRepositoryReadme(
        header: String,
        ownerName: String,
        repositoryName: String,
        branchName: String?
    ): ReadmeDto {
        return client.get("https://api.github.com/repos/$ownerName/$repositoryName/readme") {
            header("Authorization", header)

            branchName?.let {
                parameter("ref", it)
            }
        }.body()
    }
}
