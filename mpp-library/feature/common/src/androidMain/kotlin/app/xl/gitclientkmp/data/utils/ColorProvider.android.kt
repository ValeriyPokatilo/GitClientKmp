package app.xl.gitclientkmp.data.utils

import android.content.Context
import app.xl.gitclientkmp.MR
import kotlinx.serialization.json.Json

actual class ColorProvider(
    private val context: Context
) {
    private val hexRadix: Int = 16
    internal val jsonFormat: Json = Json { ignoreUnknownKeys = true }
    private val colors: Map<String, Int> by lazy {
        val json: String = MR.files.color_map_json.readText(context)

        val stringMap: Map<String, String> =
            jsonFormat.decodeFromString(json)

        stringMap.mapValues { parseColor(it.value) }
    }

    actual fun getColor(language: String): Int {
        return colors[language] ?: DEFAULT_COLOR
    }

    private fun parseColor(colorString: String): Int {
        val hex: String = colorString.removePrefix("#")
        val fullHex: String = when (hex.length) {
            HEX_LENGTH_RGB -> "FF$hex"
            HEX_LENGTH_ARGB -> hex
            else -> DEFAULT_HEX
        }
        return fullHex.toLong(hexRadix).toInt()
    }

    companion object {
        private const val DEFAULT_COLOR = 0xFFFFFFFF.toInt()
        private const val HEX_LENGTH_RGB = 6
        private const val HEX_LENGTH_ARGB = 8
        private const val DEFAULT_HEX = "FFFFFFFF"
    }
}
