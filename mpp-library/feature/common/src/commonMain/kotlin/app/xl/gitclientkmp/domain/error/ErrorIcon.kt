package app.xl.gitclientkmp.domain.error

sealed class ErrorIcon {
    object Network : ErrorIcon()
    object Http : ErrorIcon()
}
