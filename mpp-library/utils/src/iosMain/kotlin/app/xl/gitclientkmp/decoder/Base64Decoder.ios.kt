@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package app.xl.gitclientkmp.decoder

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.NSDataBase64DecodingIgnoreUnknownCharacters
import platform.Foundation.create
import platform.posix.memcpy

actual object Base64Decoder {
    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    actual fun decode(encoded: String): ByteArray {
        val data = NSData.create(
            base64EncodedString = encoded,
            options = NSDataBase64DecodingIgnoreUnknownCharacters
        )

        val result = if (data != null && data.length.toInt() > 0) {
            val length = data.length.toInt()
            val array = ByteArray(size = length)
            array.usePinned { pinned ->
                memcpy(__dst = pinned.addressOf(0), __src = data.bytes, __n = data.length)
            }
            array
        } else {
            byteArrayOf()
        }

        return result
    }
}
