package app.xl.gitclientkmp.data.network

import app.xl.gitclientkmp.data.dto.UserInfoDto

interface GitHubApi {
    suspend fun getUser(token: String): UserInfoDto
}
