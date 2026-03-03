package org.example.app.model

import androidx.compose.runtime.Composable
import org.example.android.uikit.state.model.LoadingStateData

//TODO: Add localization
@Composable
fun defaultLoadingStateData() = LoadingStateData(
    text = "Loading", //stringResource(R.string.state_loading_text),
)
