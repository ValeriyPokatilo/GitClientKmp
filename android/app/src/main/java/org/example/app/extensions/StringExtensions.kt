package org.example.app.extensions

fun String.toDisplayUrl(): String {
    return this.removePrefix("https://").removePrefix("http://")
}
