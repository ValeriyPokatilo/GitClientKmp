package org.example.android.uikit.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Class [AppColors] contains the colors from the IceBerg Design System.
 * It's not ColorScheme, since its structure is different from Material ColorScheme (see [materialColorScheme])
 */
@Immutable
data class AppColors(
    val primary: Color,
    val onPrimary: Color,
    val primaryFocus: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color,
    val inversePrimary: Color,
    val secondary: Color,
    val onSecondary: Color,
    val secondaryContainer: Color,
    val onSecondaryContainer: Color,
    val secondaryFocus: Color,
    val error: Color,
    val onError: Color,
    val errorContainer: Color,
    val onErrorContainer: Color,
    val valid: Color,
    val onValid: Color,
    val validContainer: Color,
    val onValidContainer: Color,
    val warning: Color,
    val onWarning: Color,
    val warningContainer: Color,
    val onWarningContainer: Color,
    val background: Color,
    val onBackground: Color,
    val surface: Color,
    val onSurface: Color,
    val surfaceVariant: Color,
    val onSurfaceVariant: Color,
    val surfaceContainerLowest: Color,
    val surfaceContainerLow: Color,
    val surfaceContainer: Color,
    val surfaceContainerHigh: Color,
    val surfaceContainerHighest: Color,
    val inverseSurface: Color,
    val inverseOnSurface: Color,
    val outline: Color,
    val outlineVariant: Color,
    val scrim: Color,
    val bottomNavBar: Color,
    val transparent: Color,
    val link: Color,
)

internal val LocalAppColors: ProvidableCompositionLocal<AppColors> =
    staticCompositionLocalOf {
        error("LocalAppColors not initialized yet")
    }

internal val appLightColors = AppColors(
    primary = Color(0xFF8135C5),
    onPrimary = Color(0xFFFFFFFF),
    primaryFocus = Color(0xFF9C52E1),
    primaryContainer = Color(0xFFF0DBFF),
    onPrimaryContainer = Color(0xFF2C0050),
    inversePrimary = Color(0xFFDEB7FF),
    secondary = Color(0xFF665A6F),
    onSecondary = Color(0xFFFFFFFF),
    secondaryFocus = Color(0xFFD1C1D9),
    secondaryContainer = Color(0xFFEDDDF6),
    onSecondaryContainer = Color(0xFF211829),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    valid = Color(0xFF3F6900),
    onValid = Color(0xFFFFFFFF),
    validContainer = Color(0xFFBFF281),
    onValidContainer = Color(0xFF102000),
    warning = Color(0xFFDC7614),
    onWarning = Color(0xFFFFFFFF),
    warningContainer = Color(0xFFFFDCC5),
    onWarningContainer = Color(0xFF301400),
    background = Color(0xFFFFFBFF),
    onBackground = Color(0xFF1B1B23),
    surface = Color(0xFFFBF9FF),
    onSurface = Color(0xFF1A1B1F),
    surfaceVariant = Color(0xFFE4E1EC),
    onSurfaceVariant = Color(0xFF47464F),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFEEEDF1),
    surfaceContainer = Color(0xFFE3E2E6),
    surfaceContainerHigh = Color(0xFFC7C6CA),
    surfaceContainerHighest = Color(0xFFABABAF),
    inverseSurface = Color(0xFF2F3033),
    inverseOnSurface = Color(0xFFEEEDF1),
    outline = Color(0xFF77777A),
    outlineVariant = Color(0xFFC8C5D0),
    scrim = Color(0xFF000000),
    bottomNavBar = Color(0xFFFFFFFF),
    transparent = Color(0x00FFFFFF),
    link = Color(0xFF007AFF),
)

internal val appDarkColors = AppColors(
    primary = Color(0xFFDEB7FF),
    onPrimary = Color(0xFF4A007F),
    primaryFocus = Color(0xFFCB93FF),
    primaryContainer = Color(0xFF670FAC),
    onPrimaryContainer = Color(0xFFF0DBFF),
    inversePrimary = Color(0xFF8135C5),
    secondary = Color(0xFFD1C1D9),
    onSecondary = Color(0xFF372C3F),
    secondaryFocus = Color(0xFF665A6F),
    secondaryContainer = Color(0xFF4E4356),
    onSecondaryContainer = Color(0xFFEDDDF6),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    valid = Color(0xFFBFF281),
    onValid = Color(0xFF1E3700),
    validContainer = Color(0xFF2E4F00),
    onValidContainer = Color(0xFFBFF281),
    warning = Color(0xFFFFB783),
    onWarning = Color(0xFF4F2500),
    warningContainer = Color(0xFF703700),
    onWarningContainer = Color(0xFFFFDCC5),
    background = Color(0xFF1B1B23),
    onBackground = Color(0xFFFFFBFF),
    surface = Color(0xFF1A1B1F),
    onSurface = Color(0xFFE3E2E6),
    surfaceVariant = Color(0xFF47464F),
    onSurfaceVariant = Color(0xFFC8C5D0),
    surfaceContainerLowest = Color(0xff0c0c0c),
    surfaceContainerLow = Color(0xFF2F3033),
    surfaceContainer = Color(0xFF46474A),
    surfaceContainerHigh = Color(0xFF5E5E62),
    surfaceContainerHighest = Color(0xFF77777A),
    inverseSurface = Color(0xFFE3E2E6),
    inverseOnSurface = Color(0xFF2F3033),
    outline = Color(0xFF919094),
    outlineVariant = Color(0xFFC8C5D0),
    scrim = Color(0xFF000000),
    bottomNavBar = Color(0xFF2F3033),
    transparent = Color(0x00000000),
    link = Color(0xFF007AFF),
)

/**
 * Special color (not from app palette!) to indicate places
 * where corresponding color is not specified in the Material ColorScheme
 * (it's useful for further improvement of IceBerg Design System)
 */
private val unspecifiedColor: Color = Color(0xFF00FFEA)

/**
 * Material ColorScheme is based on [AppColors] and used to apply corresponding colors
 * (primary, secondary, etc.) in material components (textFields, checkboxes, etc.) by default.
 * Colors not specified in [AppColors] should be equal to [unspecifiedColor]
 */
internal fun materialColorScheme(appColors: AppColors): ColorScheme = ColorScheme(
    primary = appColors.primary,
    onPrimary = appColors.onPrimary,
    primaryContainer = appColors.primaryContainer,
    onPrimaryContainer = appColors.onPrimaryContainer,
    inversePrimary = appColors.inversePrimary,
    secondary = appColors.secondary,
    onSecondary = appColors.onSecondary,
    secondaryContainer = appColors.secondaryContainer,
    onSecondaryContainer = appColors.onSecondaryContainer,
    tertiary = unspecifiedColor,
    onTertiary = unspecifiedColor,
    tertiaryContainer = unspecifiedColor,
    onTertiaryContainer = unspecifiedColor,
    background = appColors.background,
    onBackground = appColors.onBackground,
    surface = appColors.surface,
    onSurface = appColors.onSurface,
    surfaceVariant = appColors.surfaceVariant,
    onSurfaceVariant = appColors.onSurfaceVariant,
    surfaceTint = appColors.surfaceContainerLow,
    inverseSurface = appColors.inverseSurface,
    inverseOnSurface = appColors.inverseOnSurface,
    error = appColors.error,
    onError = appColors.onError,
    errorContainer = appColors.errorContainer,
    onErrorContainer = appColors.onErrorContainer,
    outline = appColors.outline,
    outlineVariant = appColors.outlineVariant,
    scrim = appColors.scrim,
    surfaceBright = unspecifiedColor,
    surfaceContainer = appColors.surfaceContainer,
    surfaceContainerHigh = appColors.surfaceContainerHigh,
    surfaceContainerHighest = appColors.surfaceContainerHighest,
    surfaceContainerLow = appColors.surfaceContainerLow,
    surfaceContainerLowest = appColors.surfaceContainerLowest,
    surfaceDim = unspecifiedColor,
)
