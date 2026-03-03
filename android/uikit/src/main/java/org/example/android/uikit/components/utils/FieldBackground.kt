package org.example.android.uikit.components.utils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
internal fun FieldBackground(
    enabled: Boolean,
    focused: Boolean,
    shape: Shape,
    indicatorColor: Color,
    containerColor: Color,
    modifier: Modifier = Modifier,
    focusedBorderThickness: Dp = 2.dp,
    unfocusedBorderThickness: Dp = 1.dp,
    content: @Composable (BoxScope.() -> Unit)? = null,
) {
    val thickness = when {
        !enabled -> unfocusedBorderThickness
        focused -> focusedBorderThickness
        else -> unfocusedBorderThickness
    }

    val boxModifier = modifier
        .border(
            border = BorderStroke(thickness, indicatorColor),
            shape = shape
        )
        .drawWithCache {
            val outline = shape.createOutline(size, layoutDirection, this)
            onDrawBehind { drawOutline(outline, color = containerColor) }
        }

    Box(
        modifier = boxModifier,
        content = content ?: {}
    )
}
