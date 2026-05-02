package app.xl.gitclientkmp.data.repository.mappers

import app.xl.gitclientkmp.Owner
import app.xl.gitclientkmp.data.dto.OwnerDto

fun OwnerDto.toEntity(): Owner {
    return Owner(
        login = this.login
    )
}
