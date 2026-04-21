package app.xl.gitclientkmp.domain.error

import dev.icerock.moko.resources.desc.StringDesc

data class ErrorModel(
    val title: StringDesc,
    val message: StringDesc,
    val isNetworkError: Boolean
)
