package app.xl.gitclientkmp.domain.entity

data class Repository(
    val id: Long,
    val name: String,
    val owner: Owner,
    val language: String?,
    val languageColor: Int? = null,
    val descriptionText: String?,
    val defaultBranch: String
)
