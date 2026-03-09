package app.xl.gitclientkmp.data.network

fun String.toBearerHeader(): String {
    return "Bearer ${this.trim()}"
}
