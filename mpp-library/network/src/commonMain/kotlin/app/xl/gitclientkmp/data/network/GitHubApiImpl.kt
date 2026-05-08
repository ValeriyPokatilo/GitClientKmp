package app.xl.gitclientkmp.data.network

import app.xl.gitclientkmp.data.dto.IssueDto
import app.xl.gitclientkmp.data.dto.ReadmeDto
import app.xl.gitclientkmp.data.dto.RepoDetailsDto
import app.xl.gitclientkmp.data.dto.RepoDto
import app.xl.gitclientkmp.data.dto.UserInfoDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.HttpHeaders

class GitHubApiImpl(
    private val client: HttpClient
) : GitHubApi {

    override suspend fun getUser(token: String): UserInfoDto {
        return client.get(urlString = "user") {
            header(key = HttpHeaders.Authorization, value = "Bearer $token")
        }.body()
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

    override suspend fun getIssues(
        ownerName: String,
        repositoryName: String,
        pageSize: Int,
        page: Int
    ): List<IssueDto> {
        return client.get("repos/$ownerName/$repositoryName/issues") {
            parameter(key = "state", value = "all")
            parameter(key = "page", value = page)
            parameter(key = "per_page", value = pageSize)
            parameter(key = "sort", value = "updated")
            parameter(key = "direction", value = "desc")
        }.body()
    }

    override suspend fun getIssue(
        ownerName: String,
        repositoryName: String,
        issueNumber: Int
    ): IssueDto {
        return client.get(urlString = "repos/$ownerName/$repositoryName/issues/$issueNumber").body()
    }
}
