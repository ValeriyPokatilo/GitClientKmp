package app.xl.androidapp.app.xl.gitclientkmp.data.repository.mappers

import app.xl.androidapp.app.xl.gitclientkmp.entity.UserInfo

fun app.xl.androidapp.app.xl.gitclientkmp.data.dto.UserInfoDto.toEntity(): UserInfo {
    return UserInfo(
        login = this.login
    )
}