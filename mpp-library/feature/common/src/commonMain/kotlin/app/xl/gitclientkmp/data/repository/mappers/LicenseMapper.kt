package app.xl.androidapp.app.xl.gitclientkmp.data.repository.mappers

import app.xl.androidapp.app.xl.gitclientkmp.entity.License

fun app.xl.androidapp.app.xl.gitclientkmp.data.dto.LicenseDto?.toEntity(): License? {
    return this?.let {
        License(
            it.name,
            it.url
        )
    }
}