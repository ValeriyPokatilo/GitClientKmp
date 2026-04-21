package org.example.library.di.modules

import app.xl.gitclientkmp.presentation.AuthViewModel
import org.koin.core.Koin

fun Koin.getAuthViewModel(): AuthViewModel {
    return get()
}
