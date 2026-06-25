package app.xl.gitclientkmp.logger

import io.github.aakira.napier.LogLevel
import io.github.aakira.napier.log

object Logger {
    fun info(message: String) {
        log(LogLevel.INFO, null, "GitApp", { message })
    }
}
