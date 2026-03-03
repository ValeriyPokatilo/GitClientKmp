package org.example.uisamples.typography

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp
import org.example.android.uisamples.R

private const val WEIGHT_510 = 510
private const val WEIGHT_590 = 590

val mainTextFont = FontFamily(
    Font(R.font.roboto_flex)
)

private val sfPro = FontFamily(
    Font(R.font.sf_pro)
)

private val sfCompat = FontFamily(
    Font(R.font.sf_compact)
)

@Immutable
data class BizonTypography(val mainTextFont: FontFamily) {
    val title: TitleTypography = TitleTypography(mainTextFont)
    val primary: PrimaryTypography = PrimaryTypography(mainTextFont)
    val secondary: SecondaryTypography = SecondaryTypography(mainTextFont)
    val mobile: MobileTypography = MobileTypography(mainTextFont)
}

@Immutable
data class MobileTypography(
    val mainTextFont: FontFamily,
) {
    val text: MobileLabelTypography = MobileLabelTypography(mainTextFont)
    val m3: M3Typography = M3Typography(mainTextFont)
    val custom: CustomTypography = CustomTypography(mainTextFont)
}

@Immutable
data class TitleTypography(
    val mainTextFont: FontFamily,
) {
    val large: TextStyle = TextStyle(
        fontSize = 22.sp,
        lineHeight = 28.sp,
        fontFamily = mainTextFont,
        fontWeight = FontWeight.W400
    ).preciseLineHeight()

    val headlineSmall: TextStyle = TextStyle(
        fontSize = 24.sp,
        lineHeight = 32.sp,
        fontFamily = mainTextFont,
        fontWeight = FontWeight.W400
    ).preciseLineHeight()

    val labelLarge: TextStyle = TextStyle(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontFamily = mainTextFont,
        fontWeight = FontWeight.W500
    ).preciseLineHeight()

    val bodyMedium: TextStyle = TextStyle(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontFamily = mainTextFont,
        fontWeight = FontWeight.W400
    ).preciseLineHeight()
}

@Suppress("VariableNaming")
@Immutable
data class CustomTypography(
    val mainTextFont: FontFamily,
) {
    val W400_16_22: TextStyle = TextStyle(
        fontSize = 16.sp,
        lineHeight = 22.sp,
        fontFamily = mainTextFont,
        fontWeight = FontWeight.W400
    ).preciseLineHeight()

    val W600_20_25: TextStyle = TextStyle(
        fontSize = 20.sp,
        lineHeight = 25.sp,
        fontFamily = mainTextFont,
        fontWeight = FontWeight.W600
    ).preciseLineHeight()

    val W400_20_25: TextStyle = TextStyle(
        fontSize = 20.sp,
        lineHeight = 25.36.sp,
        fontFamily = mainTextFont,
        fontWeight = FontWeight.W400
    ).preciseLineHeight()

    val W400_22_22: TextStyle = TextStyle(
        fontSize = 22.sp,
        lineHeight = 22.sp,
        fontFamily = mainTextFont,
        fontWeight = FontWeight.W400
    ).preciseLineHeight()

    val W400_24_28: TextStyle = TextStyle(
        fontSize = 24.sp,
        lineHeight = 28.sp,
        fontFamily = mainTextFont,
        fontWeight = FontWeight.W400
    ).preciseLineHeight()

    val W500_22_26: TextStyle = TextStyle(
        fontSize = 22.sp,
        lineHeight = 26.sp,
        fontFamily = mainTextFont,
        fontWeight = FontWeight.W500
    ).preciseLineHeight()

    val W500_22_28: TextStyle = TextStyle(
        fontSize = 22.sp,
        lineHeight = 28.sp,
        fontFamily = mainTextFont,
        fontWeight = FontWeight.W500
    ).preciseLineHeight()

    val W500_18_24: TextStyle = TextStyle(
        fontSize = 18.sp,
        lineHeight = 24.sp,
        fontFamily = mainTextFont,
        fontWeight = FontWeight.W500
    ).preciseLineHeight()

    val W500_18_2282: TextStyle = TextStyle(
        fontSize = 18.sp,
        lineHeight = 22.82.sp,
        fontFamily = mainTextFont,
        fontWeight = FontWeight.W500
    ).preciseLineHeight()

    val W500_16_20: TextStyle = TextStyle(
        fontSize = 16.sp,
        lineHeight = 20.sp,
        fontFamily = mainTextFont,
        fontWeight = FontWeight.W500
    ).preciseLineHeight()

    val W400_16_18: TextStyle = TextStyle(
        fontSize = 16.sp,
        lineHeight = 18.sp,
        fontFamily = mainTextFont,
        fontWeight = FontWeight.W400
    ).preciseLineHeight()

    val W400_14_18_SF: TextStyle = TextStyle(
        fontSize = 14.sp,
        lineHeight = 18.sp,
        fontFamily = sfPro,
        fontWeight = FontWeight.W400
    ).preciseLineHeight()

    val W600_18_24: TextStyle = TextStyle(
        fontSize = 18.sp,
        lineHeight = 24.sp,
        fontFamily = mainTextFont,
        fontWeight = FontWeight.W600
    ).preciseLineHeight()

    val W510_18_24: TextStyle = TextStyle(
        fontSize = 18.sp,
        lineHeight = 24.sp,
        fontFamily = sfPro,
        fontWeight = FontWeight(WEIGHT_590)
    ).preciseLineHeight()

    val W600_16_22: TextStyle = TextStyle(
        fontSize = 16.sp,
        lineHeight = 22.sp,
        fontFamily = mainTextFont,
        fontWeight = FontWeight.W600
    ).preciseLineHeight()

    val W600_14_20: TextStyle = TextStyle(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontFamily = mainTextFont,
        fontWeight = FontWeight.W600
    ).preciseLineHeight()

    val W400_14_16: TextStyle = TextStyle(
        fontSize = 14.sp,
        lineHeight = 16.sp,
        fontFamily = mainTextFont,
        fontWeight = FontWeight.W400
    ).preciseLineHeight()

    val W700_19_22: TextStyle = TextStyle(
        fontSize = 19.sp,
        lineHeight = 22.sp,
        fontFamily = mainTextFont,
        fontWeight = FontWeight.W700
    ).preciseLineHeight()
}

@Immutable
data class MobileLabelTypography(
    val mainTextFont: FontFamily,
) {
    val title: TextStyle = TextStyle(
        fontSize = 32.sp,
        lineHeight = 44.sp,
        fontWeight = FontWeight.W600,
        fontFamily = mainTextFont
    ).preciseLineHeight()

    val subtitle: TextStyle = TextStyle(
        fontSize = 20.sp,
        lineHeight = 23.4.sp,
        fontWeight = FontWeight.W600,
        fontFamily = mainTextFont
    ).preciseLineHeight()

    val addressErrorTitle: TextStyle = TextStyle(
        fontSize = 20.sp,
        lineHeight = 28.sp,
        fontWeight = FontWeight.W600,
        fontFamily = mainTextFont
    ).preciseLineHeight()

    val subLabel: TextStyle = TextStyle(
        fontSize = 14.sp,
        lineHeight = 18.sp,
        fontWeight = FontWeight.W400,
        fontFamily = mainTextFont
    ).preciseLineHeight()

    val label: TextStyle = TextStyle(
        fontSize = 16.sp,
        lineHeight = 22.sp,
        fontWeight = FontWeight.W400,
        fontFamily = mainTextFont
    ).preciseLineHeight()

    val buttonText: TextStyle = TextStyle(
        fontSize = 16.sp,
        lineHeight = 18.75.sp,
        fontWeight = FontWeight.W600,
        fontFamily = mainTextFont
    ).preciseLineHeight()

    val buttonLabel: TextStyle = TextStyle(
        fontSize = 16.sp,
        lineHeight = 19.sp,
        fontWeight = FontWeight(WEIGHT_590),
        fontFamily = sfPro
    ).preciseLineHeight()

    val hint: TextStyle = TextStyle(
        fontSize = 16.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.W400,
        fontFamily = mainTextFont
    ).preciseLineHeight()

    val smallHint: TextStyle = TextStyle(
        fontSize = 12.sp,
        lineHeight = 16.sp,
        fontWeight = FontWeight.W400,
        fontFamily = mainTextFont
    ).preciseLineHeight()

    val smallHintW500: TextStyle = TextStyle(
        fontSize = 12.sp,
        lineHeight = 16.sp,
        fontWeight = FontWeight.W500,
        fontFamily = mainTextFont
    ).preciseLineHeight()

    val errorTitle: TextStyle = TextStyle(
        fontSize = 16.sp,
        lineHeight = 22.sp,
        fontWeight = FontWeight.W400,
        fontFamily = mainTextFont
    ).preciseLineHeight()

    val errorTitleSfPro: TextStyle = TextStyle(
        fontSize = 16.sp,
        lineHeight = 22.sp,
        fontWeight = FontWeight.W400,
        fontFamily = sfPro
    ).preciseLineHeight()

    val errorDescription: TextStyle = TextStyle(
        fontSize = 14.sp,
        lineHeight = 18.sp,
        fontWeight = FontWeight.W400,
        fontFamily = mainTextFont
    ).preciseLineHeight()

    val addressLabel: TextStyle = TextStyle(
        fontSize = 16.sp,
        lineHeight = 18.sp,
        fontWeight = FontWeight.W400,
        fontFamily = mainTextFont
    ).preciseLineHeight()
}

@Immutable
data class PrimaryTypography(
    val mainTextFont: FontFamily,
) {
    val bold: TextStyle = TextStyle(
        fontSize = 20.sp,
        lineHeight = 28.sp,
        fontFamily = sfPro,
        fontWeight = FontWeight.W700
    ).preciseLineHeight()

    val semiBold: TextStyle = TextStyle(
        fontSize = 20.sp,
        lineHeight = 28.sp,
        fontFamily = mainTextFont,
        fontWeight = FontWeight.W600
    ).preciseLineHeight()

    val mediumBold: TextStyle = TextStyle(
        fontSize = 18.sp,
        lineHeight = 24.sp,
        fontFamily = sfPro,
        fontWeight = FontWeight.W700
    ).preciseLineHeight()

    val mediumW510: TextStyle = TextStyle(
        fontSize = 18.sp,
        lineHeight = 24.sp,
        fontFamily = sfPro,
        fontWeight = FontWeight(WEIGHT_510)
    ).preciseLineHeight()

    val mediumSemiBold: TextStyle = TextStyle(
        fontSize = 18.sp,
        lineHeight = 24.sp,
        fontFamily = mainTextFont,
        fontWeight = FontWeight.W600
    ).preciseLineHeight()

    val medium: TextStyle = TextStyle(
        fontSize = 18.sp,
        lineHeight = 22.sp,
        fontFamily = mainTextFont,
        fontWeight = FontWeight.W400
    ).preciseLineHeight()

    val mediumW500: TextStyle = TextStyle(
        fontSize = 18.sp,
        lineHeight = 24.sp,
        fontFamily = mainTextFont,
        fontWeight = FontWeight.W500
    ).preciseLineHeight()

    val mediumW600: TextStyle = TextStyle(
        fontSize = 18.sp,
        lineHeight = 24.sp,
        fontFamily = mainTextFont,
        fontWeight = FontWeight.W600
    ).preciseLineHeight()

    val small: TextStyle = TextStyle(
        fontSize = 16.sp,
        lineHeight = 22.sp,
        fontFamily = mainTextFont,
        fontWeight = FontWeight.W400
    ).preciseLineHeight()
}

@Immutable
data class SecondaryTypography(
    val mainTextFont: FontFamily,
) {
    val small: TextStyle = TextStyle(
        fontSize = 16.sp,
        lineHeight = 22.sp,
        fontFamily = mainTextFont,
        fontWeight = FontWeight.W400
    ).preciseLineHeight()

    val extraSmall: TextStyle = TextStyle(
        fontSize = 14.sp,
        lineHeight = 18.sp,
        fontFamily = mainTextFont,
        fontWeight = FontWeight.W400
    ).preciseLineHeight()
}

@Immutable
data class M3Typography(
    val mainTextFont: FontFamily,
) {
    val headlineSmall: TextStyle = TextStyle(
        fontSize = 24.sp,
        lineHeight = 32.sp,
        fontFamily = mainTextFont,
        fontWeight = FontWeight.W400
    ).preciseLineHeight()

    val headlineSmallW600: TextStyle = TextStyle(
        fontSize = 24.sp,
        lineHeight = 32.sp,
        fontFamily = mainTextFont,
        fontWeight = FontWeight.W600,
    ).preciseLineHeight()

    val bodyLarge: TextStyle = TextStyle(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        fontFamily = mainTextFont,
        fontWeight = FontWeight.W400,
    ).preciseLineHeight()

    val bodyMedium: TextStyle = TextStyle(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontFamily = mainTextFont,
        fontWeight = FontWeight.W400
    ).preciseLineHeight()

    val bodyMediumW600: TextStyle = TextStyle(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontFamily = mainTextFont,
        fontWeight = FontWeight.W600,
    ).preciseLineHeight()

    val bodyMediumSfCompat: TextStyle = TextStyle(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontFamily = sfCompat,
        fontWeight = FontWeight.W400
    ).preciseLineHeight()

    val bodyMediumSfCompatW900: TextStyle = TextStyle(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontFamily = sfCompat,
        fontWeight = FontWeight.W900
    ).preciseLineHeight()

    val bodySmall: TextStyle = TextStyle(
        fontSize = 12.sp,
        lineHeight = 16.sp,
        fontFamily = mainTextFont,
        fontWeight = FontWeight.W400,
    ).preciseLineHeight()

    val bodySmallSfCompat: TextStyle = TextStyle(
        fontSize = 12.sp,
        lineHeight = 26.sp,
        fontFamily = sfCompat,
        fontWeight = FontWeight.W400
    ).preciseLineHeight()

    val labelLarge: TextStyle = TextStyle(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontFamily = mainTextFont,
        fontWeight = FontWeight.W500,
    ).preciseLineHeight()

    val title: TextStyle = TextStyle(
        fontSize = 22.sp,
        lineHeight = 28.sp,
        fontFamily = mainTextFont,
        fontWeight = FontWeight.W400,
    ).preciseLineHeight()
}

fun TextStyle.preciseLineHeight(): TextStyle = this.copy(
    platformStyle = PlatformTextStyle(
        includeFontPadding = false
    ),
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None
    )
)
