package app.xl.gitclientkmp.data.repository.mappers

import app.xl.gitclientkmp.License
import app.xl.gitclientkmp.data.dto.LicenseDto

fun LicenseDto?.toEntity(): License? {
    return this?.let {
        License(
            name = it.name,
            url = it.url
        )
    }
}
