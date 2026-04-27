package app.xl.gitclientkmp.data.network

import app.xl.gitclientkmp.data.dto.ReadmeDto
import app.xl.gitclientkmp.data.dto.RepoDetailsDto
import app.xl.gitclientkmp.data.dto.RepoDto
import app.xl.gitclientkmp.data.dto.UserInfoDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class GitHubApiImpl(
    private val client: HttpClient
) : GitHubApi {

    override suspend fun getUser(): UserInfoDto {
        return client.get(urlString = "user").body()
    }

    override suspend fun getRepositories(): List<RepoDto> {
        return client.get(urlString = "user/repos").body()
    }

    override suspend fun getRepository(
        ownerName: String,
        repositoryName: String
    ): RepoDetailsDto {
        return client.get(urlString = "repos/$ownerName/$repositoryName").body()
    }

    override suspend fun getRepositoryReadme(
        ownerName: String,
        repositoryName: String,
        branchName: String?
    ): ReadmeDto {
        return client.get(urlString = "repos/$ownerName/$repositoryName/readme") {
            branchName?.let { parameter("ref", it) }
        }.body()
    }
}
