package app.xl.gitclientkmp.domain.error

import dev.icerock.moko.resources.desc.StringDesc

data class ErrorModel(
    val placeholderTitle: StringDesc,
    val placeholderMessage: StringDesc,
    val alertMessage: StringDesc? = null,
    val isNetworkError: Boolean
)
