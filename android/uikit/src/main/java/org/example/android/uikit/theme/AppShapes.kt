package org.example.android.uikit.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Immutable
data class AppShapes(
    val s: Shape,
    val m: Shape,
    val l: Shape,
    val xl: Shape,
    val xxl: Shape,
)

internal val LocalAppShapes: ProvidableCompositionLocal<AppShapes> =
    staticCompositionLocalOf {
        error("LocalComposeTypography not initialized yet")
    }

internal val appShapes = AppShapes(
    s = RoundedCornerShape(4.dp),
    m = RoundedCornerShape(8.dp),
    l = RoundedCornerShape(12.dp),
    xl = RoundedCornerShape(16.dp),
    xxl = RoundedCornerShape(24.dp),
)
