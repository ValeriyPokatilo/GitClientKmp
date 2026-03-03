package org.example.android.uikit.components.dropDownMenu

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class DropDownMenuColors(
    val containerColor: Color = Color.Unspecified,
    val defaultContentColor: Color = Color.Unspecified,
    val selectedContentColor: Color = Color.Unspecified,
)
