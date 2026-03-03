package org.example.android.uikit.state.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.painter.Painter

@Immutable
data class EmptyStateData(
    val title: String,
    val text: String,
    val buttonText: String,
    val icon: Painter?,
)
