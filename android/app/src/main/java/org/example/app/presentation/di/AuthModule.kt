package org.example.app.presentation.di

import app.xl.gitclientkmp.AuthViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val authModule = module {
    viewModel {
        AuthViewModel()
    }
}
