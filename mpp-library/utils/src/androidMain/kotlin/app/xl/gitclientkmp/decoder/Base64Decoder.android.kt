package app.xl.gitclientkmp.decoder

import java.util.Base64

actual object Base64Decoder {
    actual fun decode(encoded: String): ByteArray {
        val cleaned: String = encoded.filterNot { it.isWhitespace() }
        return Base64.getDecoder().decode(cleaned)
    }
}
