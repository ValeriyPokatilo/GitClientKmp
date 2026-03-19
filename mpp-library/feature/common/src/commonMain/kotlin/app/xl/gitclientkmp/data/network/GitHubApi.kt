package app.xl.gitclientkmp.data.network

import app.xl.gitclientkmp.data.dto.ReadmeDto
import app.xl.gitclientkmp.data.dto.RepoDetailsDto
import app.xl.gitclientkmp.data.dto.RepoDto
import app.xl.gitclientkmp.data.dto.UserInfoDto

interface GitHubApi {
    suspend fun getUser(header: String): UserInfoDto

    suspend fun getRepositories(header: String): List<RepoDto>

    suspend fun getRepository(
        ownerName: String,
        repositoryName: String
    ): RepoDetailsDto

    suspend fun getRepositoryReadme(
        ownerName: String,
        repositoryName: String,
        branchName: String? = null
    ): ReadmeDto
}
