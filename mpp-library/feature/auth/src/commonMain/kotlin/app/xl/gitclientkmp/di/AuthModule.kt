package app.xl.gitclientkmp.di

import app.xl.gitclientkmp.presentation.AuthViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val authModule: Module = module {
    factory {
        AuthViewModel(get())
    }
}
