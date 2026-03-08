package app.xl.gitclientkmp.di


import app.xl.gitclientkmp.data.repository.AppRepository
import app.xl.gitclientkmp.data.storage.KeyValueStorage
import com.russhwolf.settings.Settings
import org.koin.dsl.module

val commonModule = module {

    single<Settings> {
        Settings()
    }

    single {
        KeyValueStorage(get())
    }

    single {
        AppRepository(get())
    }
}
