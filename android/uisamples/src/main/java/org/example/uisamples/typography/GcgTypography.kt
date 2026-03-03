package org.example.uisamples.typography

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.example.android.uisamples.R

@Immutable
data class GcgTypography(
    val headerH1: TextStyle,
    val headerH2: TextStyle,
    val headerH3: TextStyle,
    val titleLarge: TextStyle,
    val titleAverage: TextStyle,
    val titleInline: TextStyle,
    val description: TextStyle,
    val descriptionMedium: TextStyle,
    val button: TextStyle,
    val regular: TextStyle,
    val segmentButton: TextStyle,
    val textFieldError: TextStyle,
    val inputContent: TextStyle,
    val inputLabel: TextStyle,
)

internal val ManropeFontFamily: FontFamily
    get() = FontFamily(
        Font(resId = R.font.manrope_extralight, weight = FontWeight.ExtraLight),
        Font(resId = R.font.manrope_light, weight = FontWeight.Light),
        Font(resId = R.font.manrope_regular, weight = FontWeight.Normal),
        Font(resId = R.font.manrope_medium, weight = FontWeight.Medium),
        Font(resId = R.font.manrope_semibold, weight = FontWeight.SemiBold),
        Font(resId = R.font.manrope_extrabold, weight = FontWeight.ExtraBold),
        Font(resId = R.font.manrope_bold, weight = FontWeight.Bold),
    )

internal val ggcTypography = GcgTypography(
    headerH1 = TextStyle(
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.W800,
        fontSize = 32.sp,
        lineHeight = 36.sp
    ),
    headerH2 = TextStyle(
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.W700,
        fontSize = 30.sp,
        lineHeight = 34.sp
    ),
    headerH3 = TextStyle(
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.W600,
        fontSize = 24.sp,
        lineHeight = 33.sp
    ),
    titleLarge = TextStyle(
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.W700,
        fontSize = 18.sp,
        lineHeight = 24.sp
    ),
    titleAverage = TextStyle(
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.W700,
        fontSize = 16.sp,
        lineHeight = 22.sp
    ),
    titleInline = TextStyle(
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.W800,
        fontSize = 20.sp,
        lineHeight = 27.sp
    ),
    description = TextStyle(
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.W400,
        fontSize = 16.sp,
        lineHeight = 22.sp
    ),
    descriptionMedium = TextStyle(
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp,
        lineHeight = 18.sp
    ),
    button = TextStyle(
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.W700,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),
    regular = TextStyle(
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.W400,
        fontSize = 14.sp,
        lineHeight = 14.sp
    ),
    segmentButton = TextStyle(
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.W600,
        fontSize = 16.sp,
        lineHeight = 20.sp,
    ),
    textFieldError = TextStyle(
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.W400,
        fontSize = 12.sp,
        lineHeight = 16.sp,
    ),
    inputContent = TextStyle(
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.W500,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),
    inputLabel = TextStyle(
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.W500,
        fontSize = 12.sp,
        lineHeight = 16.sp,
    )
)
