package org.example.library.di.modules

import dev.icerock.moko.network.generated.apis.AuthApi
import io.ktor.client.HttpClient
import org.koin.core.module.Module
import org.koin.core.scope.Scope
import org.koin.dsl.module

internal fun apiModule(baseUrl: String): Module = module {
    single<AuthApi> {
        AuthApi(
            basePath = baseUrl,
            httpClient = get(),
            json = get()
        )
    }

    // нужна отдельная фабрика для AuthApi чтобы создавать с другим HttpClient'ом для рефреша
    factory<AuthApiFactory> {
        val scope: Scope = this
        object : AuthApiFactory {
            override fun create(httpClient: HttpClient): AuthApi {
                return AuthApi(
                    basePath = baseUrl,
                    httpClient = httpClient,
                    json = scope.get()
                )
            }
        }
    }
}

internal interface AuthApiFactory {
    fun create(httpClient: HttpClient): AuthApi
}
