package app.xl.gitclientkmp

sealed class AppError(
    message: String?,
    cause: Throwable?
) : RuntimeException(message, cause) {

    class Http(
        val code: Int,
        val errorMessage: String?,
        cause: Throwable
    ) : AppError(
        message = errorMessage,
        cause = cause
    )

    class Network(
        cause: Throwable
    ) : AppError(
        message = cause.message,
        cause = cause
    )

    class Unauthorized(
        cause: Throwable
    ) : AppError(
        message = cause.message,
        cause = cause
    )
}
