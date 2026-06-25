package app.xl.gitclientkmp.data.repository.mappers

import app.xl.gitclientkmp.UserInfo
import app.xl.gitclientkmp.data.dto.UserInfoDto

fun UserInfoDto.toEntity(): UserInfo {
    return UserInfo(
        login = this.login
    )
}
