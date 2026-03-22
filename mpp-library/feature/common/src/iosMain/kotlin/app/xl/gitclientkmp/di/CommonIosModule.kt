package app.xl.gitclientkmp.di

import app.xl.gitclientkmp.data.utils.ColorProvider
import org.koin.dsl.module

val commonIosModule = module {
    single {
        ColorProvider()
    }
}
