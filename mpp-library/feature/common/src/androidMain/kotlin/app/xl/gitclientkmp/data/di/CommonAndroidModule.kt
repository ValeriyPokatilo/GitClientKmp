package app.xl.gitclientkmp.data.di

import app.xl.gitclientkmp.data.utils.ColorProvider
import org.koin.core.module.Module
import org.koin.dsl.module

val commonAndroidModule: Module = module {
    single {
        ColorProvider(context = get())
    }
}
