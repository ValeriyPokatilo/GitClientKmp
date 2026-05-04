package app.xl.gitclientkmp.di

import app.xl.gitclientkmp.colorProvider.ColorProvider
import org.koin.core.module.Module
import org.koin.dsl.module

val commonIosModule: Module = module {
    single {
        ColorProvider()
    }
}
