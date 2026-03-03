package org.example.android.uikit.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.example.android.uikit.R

@Immutable
data class AppTypography(
    val display: TypographySizeGroup,
    val headline: TypographySizeGroup,
    val title: TypographySizeGroup,
    val label: TypographySizeGroup,
    val body: TypographySizeGroup,
)

@Immutable
data class TypographySizeGroup(
    val large: TextStyle,
    val medium: TextStyle,
    val small: TextStyle,
)

private val montserratFontFamily = FontFamily(
    listOf(
        Font(R.font.montserrat_regular, FontWeight.Normal),
        Font(R.font.montserrat_medium, FontWeight.Medium),
        Font(R.font.montserrat_bold, FontWeight.Bold),
        Font(R.font.montserrat_semibold, FontWeight.SemiBold),
    )
)

internal val LocalAppTypography: ProvidableCompositionLocal<AppTypography> =
    staticCompositionLocalOf {
        error("LocalComposeTypography not initialized yet")
    }

internal val appTypography = AppTypography(
    display = TypographySizeGroup(
        large = TextStyle(
            fontFamily = montserratFontFamily,
            fontWeight = FontWeight.W400,
            fontSize = 56.sp,
            lineHeight = 68.sp
        ),
        medium = TextStyle(
            fontFamily = montserratFontFamily,
            fontWeight = FontWeight.W400,
            fontSize = 44.sp,
            lineHeight = 56.sp
        ),
        small = TextStyle(
            fontFamily = montserratFontFamily,
            fontWeight = FontWeight.W400,
            fontSize = 36.sp,
            lineHeight = 44.sp
        ),
    ),
    headline = TypographySizeGroup(
        large = TextStyle(
            fontFamily = montserratFontFamily,
            fontWeight = FontWeight.W400,
            fontSize = 32.sp,
            lineHeight = 40.sp
        ),
        medium = TextStyle(
            fontFamily = montserratFontFamily,
            fontWeight = FontWeight.W400,
            fontSize = 28.sp,
            lineHeight = 36.sp
        ),
        small = TextStyle(
            fontFamily = montserratFontFamily,
            fontWeight = FontWeight.W400,
            fontSize = 24.sp,
            lineHeight = 32.sp
        ),
    ),
    title = TypographySizeGroup(
        large = TextStyle(
            fontFamily = montserratFontFamily,
            fontWeight = FontWeight.W400,
            fontSize = 20.sp,
            lineHeight = 28.sp
        ),
        medium = TextStyle(
            fontFamily = montserratFontFamily,
            fontWeight = FontWeight.W400,
            fontSize = 16.sp,
            lineHeight = 24.sp
        ),
        small = TextStyle(
            fontFamily = montserratFontFamily,
            fontWeight = FontWeight.W400,
            fontSize = 14.sp,
            lineHeight = 20.sp
        ),
    ),
    label = TypographySizeGroup(
        large = TextStyle(
            fontFamily = montserratFontFamily,
            fontWeight = FontWeight.W500,
            fontSize = 14.sp,
            lineHeight = 20.sp
        ),
        medium = TextStyle(
            fontFamily = montserratFontFamily,
            fontWeight = FontWeight.W500,
            fontSize = 12.sp,
            lineHeight = 16.sp
        ),
        small = TextStyle(
            fontFamily = montserratFontFamily,
            fontWeight = FontWeight.W500,
            fontSize = 11.sp,
            lineHeight = 16.sp
        ),
    ),
    body = TypographySizeGroup(
        large = TextStyle(
            fontFamily = montserratFontFamily,
            fontWeight = FontWeight.W400,
            fontSize = 16.sp,
            lineHeight = 24.sp
        ),
        medium = TextStyle(
            fontFamily = montserratFontFamily,
            fontWeight = FontWeight.W400,
            fontSize = 14.sp,
            lineHeight = 20.sp
        ),
        small = TextStyle(
            fontFamily = montserratFontFamily,
            fontWeight = FontWeight.W400,
            fontSize = 12.sp,
            lineHeight = 16.sp
        ),
    ),
)
