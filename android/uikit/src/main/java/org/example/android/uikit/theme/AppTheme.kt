package org.example.android.uikit.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val themeColors = if (darkTheme) {
        appDarkColors
    } else {
        appLightColors
    }

    CompositionLocalProvider(
        LocalAppTypography provides appTypography,
        LocalAppColors provides themeColors,
        LocalAppShapes provides appShapes
    ) {
        CompositionLocalProvider(
            // Should be called after LocalAppColors is initialized
            LocalComponentColors provides componentColors()
        ) {
            MaterialTheme(
                colorScheme = materialColorScheme(themeColors),
                content = content
            )
        }
    }
}

object AppTheme {
    val colors: AppColors
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColors.current

    val shapes: AppShapes
        @Composable
        @ReadOnlyComposable
        get() = LocalAppShapes.current

    val typography: AppTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalAppTypography.current

    val componentColors: ComponentColors
        @Composable
        @ReadOnlyComposable
        get() = LocalComponentColors.current
}
