@file:Suppress("TooManyFunctions")

package org.example.android.uikit.components.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import org.example.android.uikit.R
import org.example.android.uikit.preview.MultiPreview
import org.example.android.uikit.preview.PreviewBody
import org.example.android.uikit.theme.AppTheme

@Suppress("LongMethod", "CyclomaticComplexMethod")
@Composable
fun DefaultButton(
    onClick: () -> Unit,
    style: DefaultButtonStyle,
    modifier: Modifier = Modifier,
    text: String? = null,
    icon: Painter? = null,
    enabled: Boolean = true,
    debounceClicks: Boolean = true,
    contentPadding: PaddingValues? = null,
    interactionSource: MutableInteractionSource? = null,
) {
    require(text != null || icon != null) {
        "Either text or icon (or both) should be provided"
    }

    val buttonInteractionSource = interactionSource ?: remember { MutableInteractionSource() }
    val isPressed by buttonInteractionSource.collectIsPressedAsState()

    val containerColor = if (isPressed) {
        style.pressedContainerColor()
    } else {
        style.containerColor()
    }
    val contentColor = if (isPressed) {
        style.pressedContentColor()
    } else {
        style.contentColor()
    }
    val borderColor: Color? = when {
        !enabled -> style.disabledBorderColor()
        isPressed -> style.pressedBorderColor()
        else -> style.borderColor()
    }

    val colors = ButtonDefaults.outlinedButtonColors().copy(
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = style.disabledContainerColor(),
        disabledContentColor = style.disabledContentColor(),
    )

    BaseButton(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        debounceClicks = debounceClicks,
        colors = colors,
        border = borderColor?.let {
            BorderStroke(
                width = 1.0.dp,
                color = it
            )
        },
        contentPadding = when {
            contentPadding != null -> contentPadding
            icon != null && text != null -> IconTextContentPadding
            icon != null -> IconContentPadding
            else -> TextContentPadding
        },
        interactionSource = interactionSource,
        content = {
            icon?.let {
                Icon(
                    modifier = Modifier
                        .padding(
                            end = if (text != null) 8.dp else 0.dp
                        ),
                    painter = it,
                    contentDescription = null,
                    tint = if (enabled) {
                        style.contentColor()
                    } else {
                        style.disabledContentColor()
                    },
                )
            }
            text?.let {
                Text(
                    text = it,
                    maxLines = 1,
                    style = AppTheme.typography.label.large
                )
            }
        },
    )
}

private val IconTextContentPadding = PaddingValues(
    top = 12.dp,
    bottom = 12.dp,
    start = 16.dp,
    end = 24.dp,
)

private val IconContentPadding = PaddingValues(
    all = 12.dp,
)

private val TextContentPadding = PaddingValues(
    vertical = 12.dp,
    horizontal = 24.dp,
)

class DefaultButtonStyle private constructor(
    val containerColor: @Composable () -> Color,
    val contentColor: @Composable () -> Color,
    val borderColor: @Composable () -> Color?,
    val pressedContainerColor: @Composable () -> Color,
    val pressedContentColor: @Composable () -> Color,
    val pressedBorderColor: @Composable () -> Color?,
    val disabledContainerColor: @Composable () -> Color,
    val disabledContentColor: @Composable () -> Color,
    val disabledBorderColor: @Composable () -> Color?,
) {

    companion object {

        val Filled = DefaultButtonStyle(
            containerColor = { AppTheme.colors.primary },
            contentColor = { AppTheme.colors.onPrimary },
            borderColor = { null },
            pressedContainerColor = { AppTheme.colors.primaryFocus },
            pressedContentColor = { AppTheme.colors.onPrimary },
            pressedBorderColor = { null },
            disabledContainerColor = { AppTheme.colors.surfaceContainer },
            disabledContentColor = { AppTheme.colors.outline },
            disabledBorderColor = { null },
        )

        val Tonal = DefaultButtonStyle(
            containerColor = { AppTheme.colors.secondaryContainer },
            contentColor = { AppTheme.colors.onSecondaryContainer },
            borderColor = { null },
            pressedContainerColor = { AppTheme.colors.secondaryFocus },
            pressedContentColor = { AppTheme.colors.onSecondaryContainer },
            pressedBorderColor = { null },
            disabledContainerColor = { AppTheme.colors.surfaceContainer },
            disabledContentColor = { AppTheme.colors.outline },
            disabledBorderColor = { null },
        )

        val Outlined = DefaultButtonStyle(
            containerColor = { AppTheme.colors.transparent },
            contentColor = { AppTheme.colors.primary },
            borderColor = { AppTheme.colors.primary },
            pressedContainerColor = { AppTheme.colors.secondaryContainer },
            pressedContentColor = { AppTheme.colors.primary },
            pressedBorderColor = { AppTheme.colors.primary },
            disabledContainerColor = { AppTheme.colors.transparent },
            disabledContentColor = { AppTheme.colors.outline },
            disabledBorderColor = { AppTheme.colors.outlineVariant },
        )

        val Simple = DefaultButtonStyle(
            containerColor = { AppTheme.colors.transparent },
            contentColor = { AppTheme.colors.primary },
            borderColor = { null },
            pressedContainerColor = { AppTheme.colors.secondaryContainer },
            pressedContentColor = { AppTheme.colors.primary },
            pressedBorderColor = { null },
            disabledContainerColor = { AppTheme.colors.transparent },
            disabledContentColor = { AppTheme.colors.outline },
            disabledBorderColor = { null },
        )

        val Destructive = DefaultButtonStyle(
            containerColor = { AppTheme.colors.error },
            contentColor = { AppTheme.colors.onError },
            borderColor = { null },
            pressedContainerColor = { AppTheme.colors.onErrorContainer },
            pressedContentColor = { AppTheme.colors.onError },
            pressedBorderColor = { null },
            disabledContainerColor = { AppTheme.colors.transparent },
            disabledContentColor = { AppTheme.colors.outline },
            disabledBorderColor = { null },
        )

        fun customButtonStyle(
            containerColor: @Composable () -> Color,
            contentColor: @Composable () -> Color,
            borderColor: @Composable () -> Color?,
            pressedContainerColor: @Composable () -> Color,
            pressedContentColor: @Composable () -> Color,
            pressedBorderColor: @Composable () -> Color?,
            disabledContainerColor: @Composable () -> Color,
            disabledContentColor: @Composable () -> Color,
            disabledBorderColor: @Composable () -> Color?,
        ) = DefaultButtonStyle(
            containerColor = containerColor,
            contentColor = contentColor,
            borderColor = borderColor,
            pressedContainerColor = pressedContainerColor,
            pressedContentColor = pressedContentColor,
            pressedBorderColor = pressedBorderColor,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = disabledContentColor,
            disabledBorderColor = disabledBorderColor
        )
    }
}

private class DefaultButtonStyleProvider : PreviewParameterProvider<DefaultButtonStyle> {
    override val values = sequenceOf(
        DefaultButtonStyle.Filled,
        DefaultButtonStyle.Tonal,
        DefaultButtonStyle.Outlined,
        DefaultButtonStyle.Simple,
        DefaultButtonStyle.Destructive,
    )
}

@MultiPreview
@Composable
private fun DefaultButtonPreview(
    @PreviewParameter(DefaultButtonStyleProvider::class) style: DefaultButtonStyle,
) = PreviewBody {
    DefaultButton(
        enabled = true,
        onClick = {},
        style = style,
        icon = painterResource(id = R.drawable.ic_play),
        text = "Text label",
    )
}
