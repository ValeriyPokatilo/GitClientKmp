package org.example.library.di.modules

import app.xl.gitclientkmp.presentation.RepositoriesListViewModel
import app.xl.gitclientkmp.presentation.RepositoryInfoViewModel
import org.koin.core.Koin
import org.koin.core.parameter.parametersOf

fun Koin.getRepositoriesListViewModel(): RepositoriesListViewModel {
    return get()
}

fun Koin.getRepositoryInfoViewModel(
    owner: String,
    repositoryName: String,
    branch: String
): RepositoryInfoViewModel {
    return get { parametersOf(owner, repositoryName, branch) }
}
