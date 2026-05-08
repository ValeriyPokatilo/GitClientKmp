package app.xl.gitclientkmp

enum class IssueState(val raw: String) {
    OPEN("open"),
    CLOSED("closed");

    companion object {
        fun from(rawValue: String): IssueState {
            return entries.find { it.raw == rawValue } ?: OPEN
        }
    }

    fun displayText(): String {
        return when (this) {
            OPEN -> "● Open"
            CLOSED -> "Closed"
        }
    }
}
