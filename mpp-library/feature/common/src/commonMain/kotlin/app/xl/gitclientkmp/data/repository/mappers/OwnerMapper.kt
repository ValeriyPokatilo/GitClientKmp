package app.xl.androidapp.app.xl.gitclientkmp.data.repository.mappers

import app.xl.androidapp.app.xl.gitclientkmp.entity.Owner

fun app.xl.androidapp.app.xl.gitclientkmp.data.dto.OwnerDto.toEntity(): Owner {
    return Owner(
        login = this.login
    )
}