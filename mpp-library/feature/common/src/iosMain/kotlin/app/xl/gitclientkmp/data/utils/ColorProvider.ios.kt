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
            HEX_LENGTH_RGB -> "FF$hex"
            HEX_LENGTH_ARGB -> hex
            else -> DEFAULT_HEX
        }
        return fullHex.toLong(HEX_RADIX).toInt()
    }

    companion object {
        private const val DEFAULT_COLOR = 0xFFFFFFFF.toInt()
        private const val HEX_RADIX = 16
        private const val HEX_LENGTH_RGB = 6
        private const val HEX_LENGTH_ARGB = 8
        private const val DEFAULT_HEX = "FFFFFFFF"

        private val json = Json {
            ignoreUnknownKeys = true
        }
    }
}
