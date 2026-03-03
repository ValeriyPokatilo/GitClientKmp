package org.example.uisamples.themes

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import org.example.uisamples.colors.BizonColors
import org.example.uisamples.typography.BizonTypography
import org.example.uisamples.typography.mainTextFont

@Composable
fun BizonTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        BizonColors.Dark()
    } else {
        BizonColors.Light()
    }
    CompositionLocalProvider(
        LocalColors provides colors,
        LocalTypography provides BizonTypography(mainTextFont)
    ) {
        MaterialTheme(
            content = content,
        )
    }
}

val LocalColors = compositionLocalOf<BizonColors> { BizonColors.Light() }

val LocalTypography = compositionLocalOf { BizonTypography(mainTextFont) }

object B {
    val colors: BizonColors
        @Composable
        get() = LocalColors.current
    val typography: BizonTypography
        @Composable
        get() = LocalTypography.current
}
