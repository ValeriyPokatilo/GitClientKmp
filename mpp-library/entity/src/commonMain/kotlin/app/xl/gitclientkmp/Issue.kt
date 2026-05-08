package app.xl.gitclientkmp

data class Issue(
    val id: Long,
    val title: String,
    val state: IssueState,
    val number: Int,
    val body: String,
    val updatedAt: String
)
