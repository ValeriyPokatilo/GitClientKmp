package app.xl.gitclientkmp.di

import app.xl.gitclientkmp.presentation.AuthViewModel
import org.koin.dsl.module

val authModule = module {
    factory {
        AuthViewModel(get())
    }
}
