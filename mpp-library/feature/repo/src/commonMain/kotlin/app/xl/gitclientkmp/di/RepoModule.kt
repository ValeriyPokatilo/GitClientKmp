package app.xl.gitclientkmp.di

import app.xl.gitclientkmp.viewModels.RepositoriesListViewModel
import app.xl.gitclientkmp.viewModels.RepositoryInfoViewModel
import org.koin.dsl.module

val repoModule = module {
    factory {
        RepositoriesListViewModel(
            repository = get(),
            colorProvider = get()
        )
    }

    factory { (owner: String, repositoryName: String, branch: String) ->
        RepositoryInfoViewModel(get(), owner, repositoryName, branch)
    }
}
