@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package app.xl.gitclientkmp.decoder

expect object Base64Decoder {
    fun decode(encoded: String): ByteArray
}
