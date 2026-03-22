package app.xl.gitclientkmp.data.di

import app.xl.gitclientkmp.data.utils.ColorProvider
import org.koin.dsl.module

val commonAndroidModule = module {
    single {
        ColorProvider(get())
    }
}
