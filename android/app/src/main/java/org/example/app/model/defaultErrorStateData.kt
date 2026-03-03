package org.example.app.model

import androidx.compose.runtime.Composable
import org.example.android.uikit.state.model.ErrorStateData

//TODO: Add localization
@Composable
fun defaultErrorStateData() = ErrorStateData(
    buttonText = "Retry", //stringResource(R.string.common_retry),
)
