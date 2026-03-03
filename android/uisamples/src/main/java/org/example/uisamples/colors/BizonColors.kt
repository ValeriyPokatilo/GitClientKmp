@file:Suppress("MagicNumber")

package org.example.uisamples.colors

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.uisamples.themes.B
import org.example.uisamples.themes.BizonTheme
import java.util.Locale

sealed class BizonColors {
    abstract val primary: Color
    abstract val secondary: Color
    abstract val white: Color
    abstract val black: Color
    abstract val black05: Color
    abstract val alert: Color
    abstract val accent: Color
    abstract val onPrimary: Color
    abstract val promoPriceColor: Color
    abstract val oldPriceColor: Color

    abstract val surface95: Color
    abstract val selectedSurface: Color
    abstract val border90: Color
    abstract val primaryRed: Color
    abstract val lightGrey: Color
    abstract val disabledGrey: Color
    abstract val darkGrey: Color
    abstract val disabledLabelGrey: Color
    abstract val systemError: Color
    abstract val greyScaleWhite: Color
    abstract val greySemiAlpha: Color
    abstract val blueLink: Color
    abstract val yellowLink: Color

    abstract val grey10: Color
    abstract val grey20: Color
    abstract val grey50: Color
    abstract val grey60: Color
    abstract val lightGreyNeutral60: Color
    abstract val grey70: Color

    abstract val greyScale10: Color

    abstract val shadowColor: Color

    abstract val dropShadow: Color

    abstract val transparent: Color

    abstract val borderColor: Color

    abstract val toolbarGradient: List<Color>
    abstract val tertiaryBackground: Color
    abstract val deleteButtonBackground: Color
    abstract val deleteButtonLabel: Color

    data class Light(
        override val primary: Color = Color(0xFF1B1C1C),
        override val white: Color = Color(0xFFFFFFFF),
        override val black: Color = Color(0xFF000000),
        override val black05: Color = Color(0x0D201A18),
        override val alert: Color = Color(0xFF9C1C20),
        override val accent: Color = Color(0xFF9C1C20),
        override val onPrimary: Color = Color(0xFFFFFFFF),
        override val promoPriceColor: Color = Color(0xFF9C1C20),
        override val oldPriceColor: Color = Color(0xFF1B1C1C),

        override val surface95: Color = Color(0xFFF2F0F0),
        override val selectedSurface: Color = Color(0x55000000),
        override val border90: Color = Color(0xFFE4E2E2),
        override val primaryRed: Color = Color(0xFF9C1C20),
        override val lightGrey: Color = Color(0xFFEDEDED),
        override val secondary: Color = Color(0xFF777777),
        override val disabledGrey: Color = Color(0xFFAEAEAE),
        override val darkGrey: Color = Color(0xFF8D8D8D),
        override val disabledLabelGrey: Color = Color(0xFFA1A1A1),
        override val systemError: Color = Color(0xFFEB4C4C),
        override val greyScaleWhite: Color = Color(0xFFF5A5A5),
        override val grey50: Color = Color(0xFF808080),
        override val greySemiAlpha: Color = Color(0xFFDEDEDE),
        override val blueLink: Color = Color(0xFF2F80ED),
        override val yellowLink: Color = Color(0xFFFFBB00),

        override val grey10: Color = Color(0xFFBBBBBB),
        override val grey20: Color = Color(0xFF303031),
        override val grey60: Color = Color(0xFF919091),
        override val grey70: Color = Color(0xFFACABAB),
        override val greyScale10: Color = Color(0xFF8D8E8E),

        override val transparent: Color = Color(0x00FFFFFF),

        override val shadowColor: Color = Color(0xFFF9F9F9),
        override val dropShadow: Color = Color(0xFFF8F8F8),

        override val borderColor: Color = Color(0xFF828282),

        override val toolbarGradient: List<Color> = listOf(
            Color(0xFFFF9A9E),
            Color(0xFFFAD0C4),
            Color(0xFFFAD0C4)
        ),
        override val lightGreyNeutral60: Color = Color(0xFF727B85),
        override val tertiaryBackground: Color = Color(0xFFFFFFFF),
        override val deleteButtonBackground: Color = Color(0x00FFFFFF),
        override val deleteButtonLabel: Color = Color(0xFF9C1C20),
    ) : BizonColors()

    data class Dark(
        override val primary: Color = Color(0xFF1B1C1C),
        override val white: Color = Color(0xFFFFFFFF),

        override val black: Color = Color(0xFF000000),
        override val black05: Color = Color(0x0D201A18),
        override val promoPriceColor: Color = Color(0xFF9C1C20),
        override val oldPriceColor: Color = Color(0xFF1B1C1C),

        override val surface95: Color = Color(0xFFF2F0F0),
        override val selectedSurface: Color = Color(0x55000000),
        override val border90: Color = Color(0xFFE4E2E2),
        override val primaryRed: Color = Color(0xFF9C1C20),
        override val accent: Color = Color(0xFF9C1C20),
        override val onPrimary: Color = Color(0xFFFFFFFF),
        override val alert: Color = Color(0xFF9C1C20),
        override val lightGrey: Color = Color(0xFFEDEDED),
        override val secondary: Color = Color(0xFF777777),
        override val disabledGrey: Color = Color(0xFFAEAEAE),
        override val darkGrey: Color = Color(0xFF8D8D8D),
        override val disabledLabelGrey: Color = Color(0xFFA1A1A1),
        override val systemError: Color = Color(0xFFEB4C4C),
        override val greyScaleWhite: Color = Color(0xFFF5A5A5),
        override val grey50: Color = Color(0xFF808080),
        override val greySemiAlpha: Color = Color(0xFFDEDEDE),
        override val blueLink: Color = Color(0xFF2F80ED),
        override val yellowLink: Color = Color(0xFFFFBB00),

        override val grey10: Color = Color(0xFFBBBBBB),
        override val grey20: Color = Color(0xFF303031),
        override val grey60: Color = Color(0xFF919091),
        override val grey70: Color = Color(0xFFACABAB),
        override val greyScale10: Color = Color(0xFF8D8E8E),

        override val transparent: Color = Color(0x00FFFFFF),

        override val borderColor: Color = Color(0xFF828282),

        override val shadowColor: Color = Color(0xFFF9F9F9),
        override val dropShadow: Color = Color(0xFFF8F8F8),

        override val toolbarGradient: List<Color> = listOf(
            Color(0xFFFF9A9E),
            Color(0xFFFAD0C4),
            Color(0xFFFAD0C4)
        ),
        override val lightGreyNeutral60: Color = Color(0xFF727B85),
        override val tertiaryBackground: Color = Color(0xFFEDEDED),
        override val deleteButtonBackground: Color = Color(0x00FFFFFF),
        override val deleteButtonLabel: Color = Color(0xFF9C1C20),
    ) : BizonColors()
}

private fun Color.toHexString(): String {
    val alpha = (this.alpha * 255).toInt().toString(16)
    val red = (this.red * 255).toInt().toString(16)
    val green = (this.green * 255).toInt().toString(16)
    val blue = (this.blue * 255).toInt().toString(16)
    return "#$alpha$red$green$blue".uppercase(Locale.getDefault())
}

@Composable
private fun ColorCell(color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(80.dp, 60.dp)
                .padding(4.dp)
                .background(
                    color = color,
                    shape = RoundedCornerShape(8.dp)
                )
                .clip(RoundedCornerShape(8.dp))
        )
        Text(color.toHexString())
    }
}

@Composable
private fun ColorColumn(title: String, colors: List<Color>) {
    Column(
        modifier = Modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = title, fontSize = 16.sp)
        colors.forEach {
            ColorCell(color = it)
        }
    }
}

@Suppress("LongMethod")
@Preview(showBackground = true, widthDp = 1200, heightDp = 1030)
@Composable
private fun BizonColorsPreview() {
    BizonTheme {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            ColorColumn(
                title = "Base colors",
                colors = listOf(
                    B.colors.primary,
                    B.colors.secondary,
                    B.colors.white,
                    B.colors.black,
                    B.colors.black05,
                    B.colors.alert,
                    B.colors.accent,
                    B.colors.onPrimary,
                )
            )

            VerticalDivider(
                modifier = Modifier
                    .fillMaxHeight(),
                thickness = 1.dp,
                color = Color.Black,
            )

            ColorColumn(
                title = "Price colors",
                colors = listOf(
                    B.colors.promoPriceColor,
                    B.colors.oldPriceColor,
                )
            )

            VerticalDivider(
                modifier = Modifier
                    .fillMaxHeight(),
                thickness = 1.dp,
                color = Color.Black,
            )

            ColorColumn(
                title = "Surface colors",
                colors = listOf(
                    B.colors.surface95,
                    B.colors.selectedSurface,
                    B.colors.border90,
                    B.colors.primaryRed,
                    B.colors.systemError,
                    B.colors.greyScaleWhite,
                    B.colors.greySemiAlpha,
                )
            )

            VerticalDivider(
                modifier = Modifier
                    .fillMaxHeight(),
                thickness = 1.dp,
                color = Color.Black,
            )

            ColorColumn(
                title = "Link colors",
                colors = listOf(
                    B.colors.blueLink,
                    B.colors.yellowLink,
                )
            )

            VerticalDivider(
                modifier = Modifier
                    .fillMaxHeight(),
                thickness = 1.dp,
                color = Color.Black,
            )

            ColorColumn(
                title = "Gray colors",
                colors = listOf(
                    B.colors.lightGrey,
                    B.colors.disabledGrey,
                    B.colors.darkGrey,
                    B.colors.disabledLabelGrey,
                    B.colors.grey10,
                    B.colors.grey20,
                    B.colors.grey60,
                    B.colors.grey70,
                    B.colors.greyScale10,
                )
            )

            VerticalDivider(
                modifier = Modifier
                    .fillMaxHeight(),
                thickness = 1.dp,
                color = Color.Black,
            )

            ColorColumn(
                title = "Other colors",
                colors = listOf(
                    B.colors.transparent,
                    B.colors.borderColor,
                    B.colors.shadowColor,
                    B.colors.dropShadow,
                    B.colors.lightGreyNeutral60,
                    B.colors.tertiaryBackground,
                    B.colors.deleteButtonBackground,
                    B.colors.deleteButtonLabel,
                )
            )
        }
    }
}
