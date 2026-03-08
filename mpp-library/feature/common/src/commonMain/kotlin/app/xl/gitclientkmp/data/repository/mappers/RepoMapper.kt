package app.xl.gitclientkmp.data.repository.mappers

import app.xl.gitclientkmp.data.dto.RepoDto
import app.xl.gitclientkmp.domain.entity.Repository

fun RepoDto.toEntity(): Repository {
    return Repository(
        id = this.id,
        name = this.name,
        owner = this.owner.toEntity(),
        language = this.language,
        description = this.description,
        defaultBranch = this.defaultBranch
    )
}
