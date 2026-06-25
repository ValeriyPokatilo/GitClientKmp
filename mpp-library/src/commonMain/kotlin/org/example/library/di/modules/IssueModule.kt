package org.example.library.di.modules

import app.xl.gitclientkmp.presentation.CreateIssueViewModel
import app.xl.gitclientkmp.presentation.IssueInfoViewModel
import app.xl.gitclientkmp.presentation.IssuesListViewModel
import org.koin.core.Koin
import org.koin.core.parameter.parametersOf

fun Koin.getIssuesListViewModel(
    owner: String,
    repositoryName: String
): IssuesListViewModel {
    return get { parametersOf(owner, repositoryName) }
}

fun Koin.getIssueInfoViewModel(
    owner: String,
    repositoryName: String,
    issueNumber: Int
): IssueInfoViewModel {
    return get { parametersOf(owner, repositoryName, issueNumber) }
}

fun Koin.getCreateIssueViewModel(
    owner: String,
    repositoryName: String
): CreateIssueViewModel {
    return get { parametersOf(owner, repositoryName) }
}
