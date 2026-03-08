package app.xl.gitclientkmp.di

import app.xl.gitclientkmp.viewModel.AuthViewModel
import org.koin.dsl.module

val authModule = module {

    factory {
        AuthViewModel(get() )
    }
}