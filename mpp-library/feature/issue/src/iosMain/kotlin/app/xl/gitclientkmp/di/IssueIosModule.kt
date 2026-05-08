package app.xl.gitclientkmp.di

import app.xl.gitclientkmp.presentation.IssuesListViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val issueIosModule: Module = module {
    factory { (owner: String, repositoryName: String) ->
        IssuesListViewModel(repository = get(), owner = owner, repositoryName = repositoryName)
    }
}
