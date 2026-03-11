package app.xl.gitclientkmp.di

import app.xl.gitclientkmp.viewModels.RepositoriesListViewModel
import app.xl.gitclientkmp.viewModels.RepositoryInfoViewModel
import org.koin.dsl.module

val repoModule = module {
    factory {
        RepositoriesListViewModel(get())
    }

    factory {
        RepositoryInfoViewModel(get())
    }
}
