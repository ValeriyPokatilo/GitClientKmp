package app.xl.gitclientkmp.data.utils

import java.util.Base64

actual object Base64Decoder {
    actual fun decode(encoded: String): ByteArray =
        Base64.getDecoder().decode(encoded)
}
