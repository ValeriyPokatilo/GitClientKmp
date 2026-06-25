package app.xl.gitclientkmp.data.repository.mappers

import app.xl.gitclientkmp.Repository
import app.xl.gitclientkmp.data.dto.RepoDto

fun RepoDto.toEntity(): Repository {
    return Repository(
        id = this.id,
        name = this.name,
        owner = this.owner.toEntity(),
        language = this.language,
        descriptionText = this.description,
        defaultBranch = this.defaultBranch
    )
}
