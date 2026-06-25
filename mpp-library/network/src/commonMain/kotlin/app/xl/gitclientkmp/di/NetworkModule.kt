package app.xl.gitclientkmp.di

import app.xl.gitclientkmp.data.network.GitHubApi
import app.xl.gitclientkmp.data.network.GitHubApiImpl
import app.xl.gitclientkmp.data.network.ImageApi
import app.xl.gitclientkmp.data.network.ImageApiImpl
import app.xl.gitclientkmp.data.network.createHttpClient
import app.xl.gitclientkmp.data.repository.AppRepositoryImpl
import app.xl.gitclientkmp.domain.repository.AppRepository
import com.russhwolf.settings.Settings
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.dsl.module

val networkModule: Module = module {
    single<Settings> {
        Settings()
    }

    single<AppRepository> {
        AppRepositoryImpl(
            api = get(),
            imageApi = get(),
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
            keyValueStorage = get()
        )
    }

    single<GitHubApi> {
        GitHubApiImpl(client = get())
    }

    single<ImageApi> {
        ImageApiImpl(client = get())
    }
}
