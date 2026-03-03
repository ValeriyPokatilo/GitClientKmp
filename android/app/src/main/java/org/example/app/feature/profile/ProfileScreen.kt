package org.example.app.feature.profile

import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import dev.icerock.moko.mvvm.flow.compose.observeAsActions
import org.example.android.utils.navigation.BottomMenuConfig
import org.example.android.utils.navigation.DefaultScreenNameExtension.defaultScreenName
import org.example.android.utils.navigation.Screen
import org.example.app.navigation.BottomBarItems
import org.example.sample.ExampleViewModel
import org.example.sample.ExampleViewModel.Actions.SameAction
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

object ProfileScreen : Screen.DefaultScreen {

    override val screenName: String = defaultScreenName()

    override fun bottomMenuConfig() = BottomMenuConfig.Visible(BottomBarItems.profile)

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content(navController: NavController, args: Bundle?) {
        val viewModel: ExampleViewModel = koinViewModel {
            parametersOf(
                ExampleViewModel.Params(
                    argument = ""
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

        Box(
            modifier = Modifier
                .background(color = Color.Gray)
                .fillMaxSize()
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "ProfileScreen"
            )
        }
    }
}
