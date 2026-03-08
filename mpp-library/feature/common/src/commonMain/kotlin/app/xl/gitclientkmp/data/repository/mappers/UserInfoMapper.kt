package app.xl.gitclientkmp.data.repository.mappers

import app.xl.gitclientkmp.data.dto.UserInfoDto
import app.xl.gitclientkmp.domain.entity.UserInfo

fun UserInfoDto.toEntity(): UserInfo {
    return UserInfo(
        login = this.login
    )
}
