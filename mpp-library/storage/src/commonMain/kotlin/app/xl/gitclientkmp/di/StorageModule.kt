package app.xl.gitclientkmp.di

import app.xl.gitclientkmp.storage.KeyValueStorage
import com.russhwolf.settings.Settings
import org.koin.core.module.Module
import org.koin.dsl.module

val storageModule: Module = module {
    single {
        KeyValueStorage(settings = get<Settings>())
    }
}
