package app.xl.androidapp.app.xl.gitclientkmp.data.repository.mappers

import app.xl.androidapp.app.xl.gitclientkmp.entity.Repository

fun app.xl.androidapp.app.xl.gitclientkmp.data.dto.RepoDto.toEntity(): Repository {
    return Repository(
        id = this.id,
        name = this.name,
        owner = this.owner.toEntity(),
        language = this.language,
        description = this.description,
        defaultBranch = this.defaultBranch
    )
}