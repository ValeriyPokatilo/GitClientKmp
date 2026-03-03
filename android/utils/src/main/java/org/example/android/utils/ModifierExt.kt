package org.example.android.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp

@Composable
fun Modifier.clickableRipple(
    bounded: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit,
): Modifier {
    return this
        .clickable(
            onClick = onClick,
            interactionSource = interactionSource,
            indication = ripple(bounded = bounded)
        )
}

@Composable
fun Modifier.safePadding(
    padding: PaddingValues,
): Modifier {
    val layoutDirection = LocalLayoutDirection.current
    return this.padding(
        top = padding.calculateTopPadding(),
        start = padding.calculateStartPadding(layoutDirection),
        end = padding.calculateEndPadding(layoutDirection),
    )
}

@Composable
fun Modifier.imePaddingOrElse(defaultPadding: Dp): Modifier {
    val imeInsets = WindowInsets.ime
    val imeBottomPx = imeInsets.getBottom(LocalDensity.current)

    return if (imeBottomPx > 0) {
        this.windowInsetsPadding(imeInsets)
    } else {
        this.padding(bottom = defaultPadding)
    }
}
