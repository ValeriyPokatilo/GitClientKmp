package org.example.library.di.modules

import app.xl.gitclientkmp.viewModels.RepositoriesListViewModel
import app.xl.gitclientkmp.viewModels.RepositoryInfoViewModel
import org.koin.core.Koin

fun Koin.getRepositoriesListViewModel(): RepositoriesListViewModel {
    return get()
}

fun Koin.getRepositoryInfoViewModel(): RepositoryInfoViewModel {
    return get()
}
