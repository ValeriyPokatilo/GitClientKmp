package org.example.android.uikit.components.navigationBar

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

@Immutable
data class NavigationBarColors(
    val backgroundColor: Color,
    val selectedItemColor: Color,
    val unselectedItemColor: Color,
    val selectedContentColor: Color,
    val unselectedContentColor: Color,
    val badgeBackgroundColor: Color,
    val badgeContentColor: Color,
) {
    @Stable
    internal fun itemColor(selected: Boolean): Color =
        if (selected) selectedItemColor else unselectedItemColor

    @Stable
    internal fun contentColor(selected: Boolean): Color =
        if (selected) selectedContentColor else unselectedContentColor
}
