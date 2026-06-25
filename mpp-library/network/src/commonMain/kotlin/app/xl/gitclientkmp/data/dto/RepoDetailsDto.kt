package app.xl.gitclientkmp.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RepoDetailsDto(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("full_name") val fullName: String,
    @SerialName("language") val language: String? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("forks_count") val forksCount: Int,
    @SerialName("stargazers_count") val stargazersCount: Int,
    @SerialName("subscribers_count") val subscribersCount: Int,
    @SerialName("html_url") val url: String,
    @SerialName("open_issues_count") val openIssuesCount: Int,
    @SerialName("license") val license: LicenseDto? = null
)
