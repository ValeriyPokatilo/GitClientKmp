package org.example.library.appRouter

import org.example.library.repositories.domain.AuthDomainRepository

class AppRouter internal constructor(
    private val authRepository: AuthDomainRepository,
) {
    fun getDestination(data: String? = null): Route {
        return when {
            !authRepository.isAuthorized() -> Route.AuthRoute
            data != null -> getArguments(data)
            else -> Route.HomeRoute
        }
    }

    @Suppress("UnusedParameter")
    private fun getArguments(data: String): Route {
        // TODO: handle data from push/deeplink
        return Route.HomeRoute
    }

    sealed interface Route {
        data object AuthRoute : Route
        data object HomeRoute : Route
    }
}
