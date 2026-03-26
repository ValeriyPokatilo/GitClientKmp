package org.example.app

import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.NavHostFragment
import org.example.app.databinding.MainActivityBinding
import org.example.library.appRouter.AppRouter
import org.koin.android.ext.android.inject

class AppActivity : FragmentActivity() {

    private val appRouter: AppRouter by inject()

    private lateinit var binding: MainActivityBinding

    private var isAppReadyForStart = false

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                lightScrim = Color.TRANSPARENT,
                darkScrim = Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.auto(
                lightScrim = Color.TRANSPARENT,
                darkScrim = Color.TRANSPARENT
            ),
        )

        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition {
            !isAppReadyForStart
        }

        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupInsets()
        setupNavigation()
    }

    private fun setupInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())

            val bottomPadding = if (imeInsets.bottom > 0) {
                imeInsets.bottom
            } else {
                systemBars.bottom
            }

            v.setPadding(systemBars.left, systemBars.top, systemBars.right, bottomPadding)
            insets
        }
    }

    private fun setupNavigation() {
        val navHost =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val navController = navHost.navController
        val navGraph = navController.navInflater.inflate(R.navigation.main_navigation)

        val route = appRouter.getDestination()

        val startDestination = when (route) {
            AppRouter.Route.AuthRoute -> R.id.authFragment
            AppRouter.Route.RepositoriesRoute -> R.id.repositoriesListFragment
        }

        navGraph.setStartDestination(startDestination)
        navController.graph = navGraph

        isAppReadyForStart = true

        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars =
            false
    }
}
