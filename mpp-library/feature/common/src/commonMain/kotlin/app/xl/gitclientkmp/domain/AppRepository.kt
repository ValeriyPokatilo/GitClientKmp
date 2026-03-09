package app.xl.gitclientkmp.domain

import app.xl.gitclientkmp.domain.entity.UserInfo

interface AppRepository {

    @Throws(Exception::class)
    suspend fun signIn(
        token: String
    ): UserInfo
}
