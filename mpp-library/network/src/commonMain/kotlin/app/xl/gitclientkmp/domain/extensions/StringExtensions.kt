package app.xl.gitclientkmp.domain.extensions

fun String.toDisplayUrl(): String {
    return this.removePrefix(prefix = "https://").removePrefix(prefix = "http://")
}
