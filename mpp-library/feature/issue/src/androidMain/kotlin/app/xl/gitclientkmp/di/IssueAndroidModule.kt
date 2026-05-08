package app.xl.gitclientkmp.di

import app.xl.gitclientkmp.presentation.IssueInfoViewModel
import app.xl.gitclientkmp.presentation.IssuesListViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val issueAndroidModule: Module = module {
    viewModel { (owner: String, repositoryName: String) ->
        IssuesListViewModel(
            repository = get(),
            owner = owner,
            repositoryName = repositoryName
        )
    }

    viewModel { (owner: String, repositoryName: String, issueNumber: Int) ->
        IssueInfoViewModel(
            repository = get(),
            owner = owner,
            repositoryName = repositoryName,
            issueNumber = issueNumber
        )
    }
}
