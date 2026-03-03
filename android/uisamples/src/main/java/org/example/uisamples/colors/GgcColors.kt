package org.example.uisamples.colors

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class GgcColors(
    val success: Color,
    val primaryAccent: Color,
    val primaryAccentLight: Color,
    val secondaryAccent: Color,
    val secondaryAccentLight: Color,
    val primaryPressed: Color,
    val secondaryPressed: Color,
    val secondaryDisabled: Color,
    val textPressed: Color,
    val divider: Color,
    val dialogSecondary: Color,
    val secondaryText: Color
)

internal val materialLightColors: ColorScheme = lightColorScheme(
    primary = Color(0xFFA577FE),
    onPrimary = Color(0xFFFCFCFC),
    secondary = Color(0xFFC0ECFF),
    onSecondary = Color(0xFF2F204B),
    surface = Color(0xFFF4F6F9),
    surfaceContainer = Color(0xFFFFFFFF),
    surfaceContainerHigh = Color(0xFFEEEBFC),
    onSurface = Color(0xFF2F204B),
    onSurfaceVariant = Color(0xFF695097),
    onBackground = Color(0xFF1B1B1E),
    outline = Color(0xFFEAE5FF),
    error = Color(0xFFEB4C4C),
    background = Color.White
)

internal val appLightColors = GgcColors(
    success = Color(0xFF219653),
    primaryAccent = Color(0xFF009CDF),
    primaryAccentLight = Color(0xFFEBF2FB),
    secondaryAccent = Color(0xFFE681EF),
    secondaryAccentLight = Color(0xFFF7EBFB),
    primaryPressed = Color(0xFF845FCB),
    secondaryPressed = Color(0xFFADD4E5),
    secondaryDisabled = Color(0xFFE0E1E8),
    textPressed = Color(0xFFC3C5C7),
    divider = Color(0xFFB0B0B0),
    dialogSecondary = Color(0xFF68548E),
    secondaryText = Color(0x4D3C3C43)
)

internal val inputTextFieldColors: TextFieldColors
    @Composable
    get() = TextFieldDefaults.colors(
        focusedContainerColor = colorScheme.outline,
        unfocusedContainerColor = colorScheme.surfaceContainerHigh,
        errorContainerColor = colorScheme.outline,
        disabledContainerColor = colorScheme.surfaceContainerHigh.copy(alpha = 0.38f),
        focusedLeadingIconColor = colorScheme.primary,
        unfocusedLeadingIconColor = colorScheme.primary,
        disabledLeadingIconColor = colorScheme.primary.copy(alpha = 0.38f),
        errorLeadingIconColor = colorScheme.primary,
        focusedTextColor = colorScheme.onSurface,
        unfocusedTextColor = colorScheme.onSurface,
        disabledTextColor = colorScheme.onSurface.copy(alpha = 0.38f),
        errorTextColor = colorScheme.onSurface,
        unfocusedLabelColor = colorScheme.onSurface,
        focusedLabelColor = colorScheme.onSurface,
        errorLabelColor = colorScheme.error,
        disabledLabelColor = colorScheme.onSurface.copy(alpha = 0.38f),
        focusedTrailingIconColor = colorScheme.primary,
        unfocusedTrailingIconColor = colorScheme.primary,
        errorTrailingIconColor = colorScheme.primary,
        disabledTrailingIconColor = colorScheme.primary.copy(alpha = 0.38f),
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        errorIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        cursorColor = colorScheme.primary,
        errorPrefixColor = colorScheme.onSurface,
        focusedPrefixColor = colorScheme.onSurface,
        disabledPrefixColor = colorScheme.onSurface.copy(alpha = 0.38f),
        unfocusedPrefixColor = colorScheme.onSurface,
        disabledPlaceholderColor = colorScheme.onSurfaceVariant,
        errorPlaceholderColor = colorScheme.onSurfaceVariant,
        focusedPlaceholderColor = colorScheme.onSurfaceVariant,
        unfocusedPlaceholderColor = colorScheme.onSurfaceVariant
    )
