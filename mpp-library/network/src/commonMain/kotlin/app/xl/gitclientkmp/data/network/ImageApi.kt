package app.xl.gitclientkmp.data.network

interface ImageApi {
    suspend fun uploadImage(bytes: ByteArray): String
}
