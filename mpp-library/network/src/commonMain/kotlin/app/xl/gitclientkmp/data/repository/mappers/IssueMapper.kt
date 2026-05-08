package app.xl.gitclientkmp.data.repository.mappers

import app.xl.gitclientkmp.Issue
import app.xl.gitclientkmp.IssueState
import app.xl.gitclientkmp.data.dto.IssueDto

fun IssueDto.toEntity(): Issue {
    return Issue(
        id = id,
        title = title,
        state = IssueState.from(state),
        number = number,
        body = body,
        updatedAt = updatedAt
    )
}
