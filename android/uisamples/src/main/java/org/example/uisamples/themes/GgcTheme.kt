package org.example.uisamples.themes

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import org.example.uisamples.colors.GgcColors
import org.example.uisamples.colors.appLightColors
import org.example.uisamples.colors.inputTextFieldColors
import org.example.uisamples.colors.materialLightColors
import org.example.uisamples.typography.GcgTypography
import org.example.uisamples.typography.ggcTypography

@Composable
fun GgcTheme(
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalAppColors provides appLightColors,
        LocalGgcTypography provides ggcTypography,
    ) {
        MaterialTheme(
            colorScheme = materialLightColors,
            content = content
        )
    }
}

val LocalAppColors: ProvidableCompositionLocal<GgcColors> =
    compositionLocalOf {
        appLightColors
    }

val LocalGgcTypography: ProvidableCompositionLocal<GcgTypography> =
    compositionLocalOf {
        ggcTypography
    }

object GgcTheme {
    val colors: GgcColors
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColors.current

    val typography: GcgTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalGgcTypography.current

    val textFieldColors: TextFieldColors
        @Composable
        get() = inputTextFieldColors
}
