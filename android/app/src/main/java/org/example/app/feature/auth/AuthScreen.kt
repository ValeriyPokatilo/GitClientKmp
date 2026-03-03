package org.example.app.feature.auth

import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import dev.icerock.moko.mvvm.flow.compose.observeAsActions
import org.example.android.utils.navigation.BottomMenuConfig
import org.example.android.utils.navigation.DefaultScreenNameExtension.defaultScreenNameWithOptionalParams
import org.example.android.utils.navigation.Screen
import org.example.android.utils.navigation.ScreenNameExtension.screenNameWithParams
import org.example.app.R
import org.example.sample.ExampleViewModel
import org.example.sample.ExampleViewModel.Actions.SameAction
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

object AuthScreen : Screen.DefaultScreen {
    private const val EXAMPLE_ID = "exampleId"

    override val navArgs: List<NamedNavArgument> = listOf(
        navArgument(EXAMPLE_ID) {
            type = NavType.StringType
            defaultValue = "example"
        }
    )

    override val screenName: String = defaultScreenNameWithOptionalParams(EXAMPLE_ID)

    fun screenName(login: String) = screenNameWithParams(login)

    override fun bottomMenuConfig() = BottomMenuConfig.Hidden

    @OptIn(ExperimentalMaterial3Api::class)
    @Suppress("UnusedPrivateMember")
    @Composable
    override fun Content(navController: NavController, args: Bundle?) {
        val arg: String = args?.getString(EXAMPLE_ID) ?: "default arg"

        val viewModel: ExampleViewModel = koinViewModel {
            parametersOf(
                ExampleViewModel.Params(
                    argument = arg
                )
            )
        }

        viewModel.actions.observeAsActions { action ->
            when (action) {
                SameAction -> {
                    //TODO
                }
            }
        }

        Column(modifier = Modifier.fillMaxSize()) {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(R.string.app_name))
                }
            )

            Box(
                modifier = Modifier
                    .background(color = Color.Gray)
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    text = "Bottom"
                )
            }
        }
    }
}
