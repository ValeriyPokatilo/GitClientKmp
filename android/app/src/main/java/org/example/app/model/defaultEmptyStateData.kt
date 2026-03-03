package org.example.app.model

import androidx.compose.runtime.Composable
import org.example.android.uikit.state.model.EmptyStateData

//TODO: Add localization
@Composable
fun defaultEmptyStateData() = EmptyStateData(
    title = "Empty state title", //stringResource(R.string.state_empty_title),
    text = "Empty state text", //stringResource(R.string.state_empty_text),
    buttonText = "Reload", //stringResource(R.string.state_empty_button),
    icon = null,
)
