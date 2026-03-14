package app.xl.gitclientkmp.domain

import app.xl.gitclientkmp.domain.entity.Repository
import app.xl.gitclientkmp.domain.entity.RepositoryDetails
import app.xl.gitclientkmp.domain.entity.UserInfo

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
    ): String?

    fun logout()
}
