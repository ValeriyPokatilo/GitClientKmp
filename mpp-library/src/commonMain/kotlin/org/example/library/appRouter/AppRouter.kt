package org.example.library.appRouter

import org.example.library.repositories.domain.AuthDomainRepository

class AppRouter internal constructor(
    private val authRepository: AuthDomainRepository,
) {
    fun getDestination(): Route {
        return if (!authRepository.isAuthorized()) {
            Route.AuthRoute
        } else {
            Route.RepositoriesRoute
        }
    }

    sealed interface Route {
        data object AuthRoute : Route
        data object RepositoriesRoute : Route
    }
}
