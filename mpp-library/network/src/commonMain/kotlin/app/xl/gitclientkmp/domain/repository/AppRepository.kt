package app.xl.gitclientkmp.domain.repository

import app.xl.gitclientkmp.Issue
import app.xl.gitclientkmp.Repository
import app.xl.gitclientkmp.RepositoryDetails
import app.xl.gitclientkmp.UserInfo

interface AppRepository {

    @Throws(Exception::class)
    suspend fun signIn(
        token: String
    ): UserInfo

    @Throws(Exception::class)
    suspend fun getRepositories(): List<Repository>

    @Throws(Exception::class)
    suspend fun getRepository(
        ownerName: String,
        repositoryName: String
    ): RepositoryDetails

    @Throws(Exception::class)
    suspend fun getRepositoryReadme(
        ownerName: String,
        repositoryName: String,
        branchName: String? = null
    ): String

    @Throws(Exception::class)
    suspend fun getIssues(
        ownerName: String,
        repositoryName: String,
        pageSize: Int,
        page: Int
    ): List<Issue>

    fun logout()
}
