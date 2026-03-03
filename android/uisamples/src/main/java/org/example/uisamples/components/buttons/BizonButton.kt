package org.example.uisamples.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import org.example.uisamples.themes.B
import org.example.uisamples.themes.BizonTheme

@Composable
fun BizonButton(
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    text: String,
    icon: Painter? = null,
    isLoading: Boolean = false,
    style: BizonButtonStyle,
    textStyle: TextStyle = B.typography.mobile.text.buttonLabel,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .background(
                color = if (isEnabled) style.backgroundColor() else B.colors.lightGrey
            )
            .clickable {
                if (isEnabled) {
                    onClick()
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon?.let {
                Icon(
                    painter = icon,
                    contentDescription = "",
                    tint = style.labelColor.invoke()
                )
                Spacer(modifier = Modifier.width(10.dp))
            }
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    color = if (isEnabled) {
                        style.labelColor()
                    } else {
                        B.colors.accent
                    },
                    trackColor = B.colors.transparent,
                    strokeWidth = 1.dp,
                    strokeCap = StrokeCap.Square
                )
            } else {
                Text(
                    text = text,
                    color = if (isEnabled) {
                        style.labelColor()
                    } else {
                        B.colors.disabledLabelGrey
                    },
                    style = textStyle
                )
            }
        }
    }
}

class BizonButtonStyle private constructor(
    val backgroundColor: @Composable () -> Color,
    val labelColor: @Composable () -> Color
) {

    companion object {
        val Primary = BizonButtonStyle(
            backgroundColor = { B.colors.primaryRed },
            labelColor = { B.colors.onPrimary }
        )

        val Secondary = BizonButtonStyle(
            backgroundColor = { B.colors.lightGrey },
            labelColor = { B.colors.primary }
        )

        val Tertiary = BizonButtonStyle(
            backgroundColor = { B.colors.tertiaryBackground },
            labelColor = { B.colors.accent }
        )

        val Delete = BizonButtonStyle(
            backgroundColor = { B.colors.deleteButtonBackground },
            labelColor = { B.colors.deleteButtonLabel }
        )
    }
}

private class BizonButtonStyleProvider : PreviewParameterProvider<BizonButtonStyle> {
    override val values = sequenceOf(
        BizonButtonStyle.Primary,
        BizonButtonStyle.Secondary,
        BizonButtonStyle.Tertiary,
        BizonButtonStyle.Delete
    )
}

@PreviewLightDark
@Composable
private fun BizonButtonPreview(
    @PreviewParameter(BizonButtonStyleProvider::class) style: BizonButtonStyle,
) {
    BizonTheme(isSystemInDarkTheme()) {
        BizonButton(
            modifier = Modifier,
            isEnabled = true,
            text = "Текст кнопки",
            style = style,
            onClick = {}
        )
    }
}
