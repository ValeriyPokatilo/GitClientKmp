package app.xl.gitclientkmp

data class RepositoryDetails(
    val id: Long,
    val name: String,
    val fullName: String,
    val language: String?,
    val description: String?,
    val forksCount: Int,
    val stargazersCount: Int,
    val subscribersCount: Int,
    val url: String,
    val openIssuesCount: Int,
    val license: License?
)
