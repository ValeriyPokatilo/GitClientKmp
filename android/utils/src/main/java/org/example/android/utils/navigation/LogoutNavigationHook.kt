package org.example.android.utils.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import io.github.aakira.napier.Napier
import org.example.library.utils.logout.LogoutHandler
import org.koin.compose.koinInject

/**
 * Привязка логики обработки события разлогина.
 */
@Composable
fun LogoutNavigationHook(navController: NavHostController, loggedOutRoute: String) {
    val handler: LogoutHandler = koinInject()

    LaunchedEffect(handler, navController) {
        handler.logoutEvents.collect {
            Napier.d("received logout event - navigate to logout route")
            navController.replace(loggedOutRoute)
        }
    }
}
