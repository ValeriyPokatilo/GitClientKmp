package app.xl.gitclientkmp.data.repository.mappers

import app.xl.gitclientkmp.data.dto.OwnerDto
import app.xl.gitclientkmp.domain.entity.Owner

fun OwnerDto.toEntity(): Owner {
    return Owner(
        login = this.login
    )
}
