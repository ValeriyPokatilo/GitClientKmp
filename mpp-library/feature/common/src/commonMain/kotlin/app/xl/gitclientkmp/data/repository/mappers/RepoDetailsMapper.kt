package app.xl.gitclientkmp.data.repository.mappers

import app.xl.gitclientkmp.data.dto.RepoDetailsDto
import app.xl.gitclientkmp.domain.entity.RepositoryDetails

fun RepoDetailsDto.toEntity(): RepositoryDetails {
    return RepositoryDetails(
        id = this.id,
        name = this.name,
        fullName = this.fullName,
        language = this.language,
        description = this.description,
        forksCount = this.forksCount,
        stargazersCount = this.stargazersCount,
        subscribersCount = this.subscribersCount,
        url = this.url,
        license = this.license?.toEntity()
    )
}
