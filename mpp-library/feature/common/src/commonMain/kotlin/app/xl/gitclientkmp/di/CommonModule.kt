package app.xl.gitclientkmp.di

import app.xl.gitclientkmp.data.network.GitHubApi
import app.xl.gitclientkmp.data.network.GitHubApiImpl
import app.xl.gitclientkmp.data.network.createHttpClient
import app.xl.gitclientkmp.data.repository.AppRepositoryImpl
import app.xl.gitclientkmp.data.storage.KeyValueStorage
import app.xl.gitclientkmp.domain.repository.AppRepository
import com.russhwolf.settings.Settings
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val commonModule = module {
    single<Settings> {
        Settings()
    }

    single {
        KeyValueStorage(get())
    }

    single<AppRepository> {
        AppRepositoryImpl(
            api = get(),
            json = get(),
            keyValueStorage = get()
        )
    }

    single {
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
    }

    single {
        createHttpClient(
            json = get(),
            tokenProvider = {
                val storage: KeyValueStorage = get()
                storage.getToken().orEmpty()
            }
        )
    }

    single<GitHubApi> {
        GitHubApiImpl(get())
    }
}
