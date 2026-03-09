package org.example.library.di.modules

import app.xl.gitclientkmp.viewModel.AuthViewModel
import org.koin.core.Koin

fun Koin.getAuthViewModel(): AuthViewModel {
    return get()
}
