package app.xl.gitclientkmp.data.utils

import app.xl.gitclientkmp.MR
import kotlinx.serialization.json.Json

actual class ColorProvider {

    private val colors: Map<String, Int> by lazy {
        val jsonString = MR.files.color_map_json.readText()

        val stringMap: Map<String, String> =
            json.decodeFromString(jsonString)

        stringMap.mapValues { parseColor(it.value) }
    }

    actual fun getColor(language: String): Int {
        return colors[language] ?: DEFAULT_COLOR
    }

    private fun parseColor(colorString: String): Int {
        val hex = colorString.removePrefix("#")
        val fullHex = when (hex.length) {
            6 -> "FF$hex"
            8 -> hex
            else -> "FFFFFFFF"
        }
        return fullHex.toLong(16).toInt()
    }

    companion object {
        private const val DEFAULT_COLOR = 0xFFFFFFFF.toInt()

        private val json = Json {
            ignoreUnknownKeys = true
        }
    }
}