package app.xl.gitclientkmp.di

import app.xl.gitclientkmp.presentation.RepositoriesListViewModel
import app.xl.gitclientkmp.presentation.RepositoryInfoViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val repoModule: Module = module {
    factory {
        RepositoriesListViewModel(
            repository = get(),
            colorProvider = get()
        )
    }

    factory { (owner: String, repositoryName: String, branch: String) ->
        RepositoryInfoViewModel(
            repository = get(),
            owner = owner,
            repositoryName = repositoryName,
            branch = branch
        )
    }
}
