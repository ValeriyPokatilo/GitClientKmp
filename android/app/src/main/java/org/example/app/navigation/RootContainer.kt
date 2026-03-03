package org.example.app.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import org.example.android.utils.navigation.BottomMenuConfig
import org.example.android.utils.navigation.LogoutNavigationHook
import org.example.android.utils.navigation.composableScreen
import org.example.app.feature.auth.AuthScreen
import org.example.app.feature.profile.ProfileScreen
import org.example.library.appRouter.AppRouter.Route

@Suppress("LongMethod")
@Composable
fun RootContainer(
    destinationRoute: Route,
) {
    val navController: NavHostController = rememberNavController()

    val startDestination: String = when (destinationRoute) {
        Route.AuthRoute -> Hosts.Auth.route
        Route.HomeRoute -> Hosts.Main.route
    }

    var bottomMenuConfig: BottomMenuConfig by remember {
        mutableStateOf(BottomMenuConfig.Hidden)
    }

    LogoutNavigationHook(
        navController = navController,
        loggedOutRoute = Hosts.Auth.route
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            (bottomMenuConfig as? BottomMenuConfig.Visible)?.let { config ->
                BottomBar(
                    navController = navController,
                    selectedItem = config.bottomItem,
                )
            }
        }
    ) { padding ->
        NavHost(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .consumeWindowInsets(padding)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Horizontal,
                    ),
                ),
            navController = navController,
            startDestination = startDestination
        ) {

            navigation(
                route = Hosts.Auth.route,
                startDestination = AuthScreen.screenName,
            ) {
                authScreens.forEach { screen ->
                    composableScreen(
                        screen = screen,
                        navController = navController,
                        updateBottomMenuConfig = {
                            bottomMenuConfig = it
                        }
                    )
                }
            }

            navigation(
                route = Hosts.Main.route,
                // TODO: add correct startDestination for Main route
                startDestination = ProfileScreen.screenName,
            ) {
                mainScreens.forEach { screen ->
                    composableScreen(
                        screen = screen,
                        navController = navController,
                        updateBottomMenuConfig = {
                            bottomMenuConfig = it
                        }
                    )
                }
            }
        }
    }
}
