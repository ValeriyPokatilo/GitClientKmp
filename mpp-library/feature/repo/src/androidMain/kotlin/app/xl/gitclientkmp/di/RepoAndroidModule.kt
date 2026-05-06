package app.xl.gitclientkmp.di

import app.xl.gitclientkmp.presentation.RepositoriesListViewModel
import app.xl.gitclientkmp.presentation.RepositoryInfoViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val repoAndroidModule: Module = module {
    viewModel {
        RepositoriesListViewModel(
            repository = get(),
            colorProvider = get()
        )
    }

    viewModel { (owner: String, repositoryName: String, branch: String) ->
        RepositoryInfoViewModel(
            repository = get(),
            owner = owner,
            repositoryName = repositoryName,
            branch = branch
        )
    }
}
