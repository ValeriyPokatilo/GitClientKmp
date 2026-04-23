package app.xl.gitclientkmp.data.repository.mappers

import app.xl.gitclientkmp.data.dto.LicenseDto
import app.xl.gitclientkmp.domain.entity.License

fun LicenseDto?.toEntity(): License? {
    return this?.let {
        License(
            name = it.name,
            url = it.url
        )
    }
}
