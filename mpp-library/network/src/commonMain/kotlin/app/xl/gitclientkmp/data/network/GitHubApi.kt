package app.xl.gitclientkmp.data.network

import app.xl.gitclientkmp.data.dto.IssueDto
import app.xl.gitclientkmp.data.dto.ReadmeDto
import app.xl.gitclientkmp.data.dto.RepoDetailsDto
import app.xl.gitclientkmp.data.dto.RepoDto
import app.xl.gitclientkmp.data.dto.UserInfoDto

interface GitHubApi {
    suspend fun getUser(token: String): UserInfoDto

    suspend fun getRepositories(): List<RepoDto>

    suspend fun getRepository(
        ownerName: String,
        repositoryName: String
    ): RepoDetailsDto

    suspend fun getRepositoryReadme(
        ownerName: String,
        repositoryName: String,
        branchName: String? = null
    ): ReadmeDto

    suspend fun getIssues(
        ownerName: String,
        repositoryName: String,
        pageSize: Int,
        page: Int,
    ): List<IssueDto>
}
