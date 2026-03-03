@file:Suppress("TopLevelPropertyNaming")

package org.example.android.uikit.components.pickerField

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import org.example.android.uikit.components.utils.FieldBackground
import org.example.android.uikit.components.utils.measure
import org.example.android.uikit.components.utils.measureOrNull
import org.example.android.uikit.theme.AppTheme
import org.example.android.utils.clickableRipple

@Suppress("LongMethod", "CyclomaticComplexMethod")
@Composable
internal fun BasePickerField(
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String? = null,
    placeholder: String? = null,
    errorText: String? = null,
    isError: Boolean = errorText != null,
    isValid: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    interactionSource: MutableInteractionSource? = null,
    colors: PickerFieldColors = PickerFieldColors(),
) {
    require(value.isNotEmpty() || !label.isNullOrEmpty() || !placeholder.isNullOrEmpty()) {
        "Specify label or placeholder to show in field when value is empty"
    }

    var isFocused: Boolean by remember { mutableStateOf(false) }

    val fieldInteractionSource = interactionSource ?: remember { MutableInteractionSource() }

    val valueColor = colors.textColor(
        enabled = enabled,
        isError = isError,
        isValid = isValid,
        focused = isFocused
    )
    val labelColor = colors.labelColor(
        enabled = enabled,
        isError = isError,
        focused = isFocused
    )
    val placeholderColor = colors.placeholderColor(
        enabled = enabled,
        isError = isError,
        focused = isFocused
    )
    val leadingIconColor = colors.leadingIconColor(
        enabled = enabled,
        isError = isError,
        focused = isFocused
    )
    val trailingIconColor = colors.trailingIconColor(
        enabled = enabled,
        isError = isError,
        focused = isFocused
    )
    val errorTextColor = colors.errorSupportingTextColor

    val showLabel = value.isNotEmpty() || !placeholder.isNullOrEmpty()

    val (text, textColor) = remember(value, placeholder, label, colors) {
        when {
            value.isNotEmpty() -> value to valueColor
            !placeholder.isNullOrEmpty() -> placeholder to placeholderColor
            !label.isNullOrEmpty() -> label to labelColor
            else -> error("Specify label or placeholder to show in field when value is empty")
        }
    }

    Layout(
        modifier = modifier
            .focusable(enabled = enabled)
            .onFocusChanged { isFocused = it.isFocused }
            .semantics(mergeDescendants = true) { role = Role.Button },
        content = {
            // Background
            PickerFieldBackground(
                modifier = Modifier
                    .layoutId(BackgroundLayoutId)
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = enabled,
                isError = isError,
                isValid = isValid,
                interactionSource = fieldInteractionSource,
                colors = colors,
                shape = AppTheme.shapes.m,
            )

            // Clickable zone
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .layoutId(ClickableZoneLayoutId)
                    .clip(AppTheme.shapes.m)
                    .clickableRipple(
                        bounded = true,
                        interactionSource = fieldInteractionSource,
                        onClick = onClick
                    ),
            )

            // Label
            if (showLabel && label != null) {
                Text(
                    modifier = Modifier
                        .wrapContentWidth()
                        .height(16.dp)
                        .layoutId(LabelLayoutId)
                        .background(AppTheme.colors.surface)
                        .padding(horizontal = 4.dp),
                    text = label,
                    maxLines = 1,
                    color = labelColor,
                    style = AppTheme.typography.body.small
                )
            }

            // Text
            Text(
                modifier = Modifier
                    .wrapContentHeight()
                    .layoutId(TextLayoutId),
                text = text,
                maxLines = 1,
                color = textColor,
                overflow = TextOverflow.Ellipsis,
                style = AppTheme.typography.body.large
            )

            // Error text
            if (errorText != null) {
                Text(
                    modifier = Modifier
                        .wrapContentHeight()
                        .layoutId(ErrorTextLayoutId),
                    text = errorText,
                    color = errorTextColor,
                    style = AppTheme.typography.body.small
                )
            }

            // Leading icon
            if (leadingIcon != null) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .layoutId(LeadingIconLayoutId),
                    contentAlignment = Alignment.Center,
                    content = {
                        CompositionLocalProvider(
                            LocalContentColor provides leadingIconColor,
                            content = leadingIcon
                        )
                    }
                )
            }

            // Trailing icon
            if (trailingIcon != null) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .layoutId(TrailingIconLayoutId),
                    contentAlignment = Alignment.Center,
                    content = {
                        CompositionLocalProvider(
                            LocalContentColor provides trailingIconColor,
                            content = trailingIcon
                        )
                    }
                )
            }
        }
    ) { measurables, constraints ->
        val topPadding = if (label != null) {
            // half of label height
            8.dp.roundToPx()
        } else {
            0
        }
        val smallPadding = 4.dp.roundToPx()
        val mediumPadding = 8.dp.roundToPx()
        val bigPadding = 12.dp.roundToPx()
        val labelHorizontalPadding = 12.dp.roundToPx()
        val errorHorizontalPadding = 16.dp.roundToPx()

        val backgroundPlaceable = measurables.measure(
            layoutId = BackgroundLayoutId,
            constraints = constraints.offset(vertical = -topPadding)
        )
        val clickableZonePlaceable = measurables.measure(
            layoutId = ClickableZoneLayoutId,
            constraints = constraints.offset(vertical = -topPadding)
        )

        val labelPlaceable = measurables.measureOrNull(
            layoutId = LabelLayoutId,
            constraints = constraints
                .copy(minWidth = 0, minHeight = 0)
                .offset(horizontal = -(labelHorizontalPadding * 2))
        )
        val errorTextPlaceable = measurables.measureOrNull(
            layoutId = ErrorTextLayoutId,
            constraints = constraints
                .copy(minWidth = 0, minHeight = 0)
                .offset(horizontal = -(errorHorizontalPadding * 2))
        )

        val leadingIconPlaceable: Placeable? = measurables.measureOrNull(
            layoutId = LeadingIconLayoutId,
            constraints = constraints.copy(minWidth = 0, minHeight = 0)
        )
        val trailingIconPlaceable: Placeable? = measurables.measureOrNull(
            layoutId = TrailingIconLayoutId,
            constraints = constraints.copy(minWidth = 0, minHeight = 0)
        )

        // Paddings for components are calculated according to paddings in BaseTextField

        val leadingIconPadding =
            leadingIconPlaceable?.let { it.width + mediumPadding } ?: bigPadding
        val trailingIconPadding = trailingIconPlaceable?.width ?: bigPadding

        val leftTextPadding = leadingIconPadding + smallPadding
        val rightTextPadding = trailingIconPadding + smallPadding

        val textPlaceable = measurables.measure(
            layoutId = TextLayoutId,
            constraints = constraints.offset(horizontal = -(leftTextPadding + rightTextPadding))
        )

        val errorHeight = errorTextPlaceable?.let { it.height + smallPadding } ?: 0
        val fieldHeight = backgroundPlaceable.height

        val width = leftTextPadding + textPlaceable.width + rightTextPadding
        val height = topPadding + fieldHeight + errorHeight

        layout(width, height) {
            backgroundPlaceable.placeRelative(x = 0, y = topPadding)

            labelPlaceable?.placeRelative(x = labelHorizontalPadding, y = 0)

            errorTextPlaceable?.placeRelative(
                x = errorHorizontalPadding,
                y = topPadding + fieldHeight + smallPadding
            )

            textPlaceable.placeRelative(
                x = leftTextPadding,
                y = Alignment.CenterVertically.align(
                    size = textPlaceable.height,
                    space = fieldHeight
                ) + topPadding
            )
            clickableZonePlaceable.placeRelative(
                x = 0,
                y = topPadding
            )

            leadingIconPlaceable?.placeRelative(
                x = smallPadding,
                y = Alignment.CenterVertically.align(
                    size = leadingIconPlaceable.height,
                    space = fieldHeight
                ) + topPadding
            )
            trailingIconPlaceable?.placeRelative(
                x = leftTextPadding + textPlaceable.width,
                y = Alignment.CenterVertically.align(
                    size = trailingIconPlaceable.height,
                    space = fieldHeight
                ) + topPadding
            )
        }
    }
}

@Composable
private fun PickerFieldBackground(
    enabled: Boolean,
    isError: Boolean,
    isValid: Boolean,
    shape: Shape,
    colors: PickerFieldColors,
    interactionSource: InteractionSource,
    modifier: Modifier = Modifier,
) {
    val focused = interactionSource.collectIsFocusedAsState().value

    FieldBackground(
        modifier = modifier,
        enabled = enabled,
        focused = focused,
        shape = shape,
        indicatorColor = colors.indicatorColor(enabled, isError, isValid, focused),
        containerColor = colors.containerColor(enabled, isError, isValid, focused),
    )
}

private const val BackgroundLayoutId = "background"
private const val ClickableZoneLayoutId = "clickableZone"
private const val LeadingIconLayoutId = "leadingIcon"
private const val TrailingIconLayoutId = "trailingIcon"
private const val LabelLayoutId = "label"
private const val TextLayoutId = "text"
private const val ErrorTextLayoutId = "errorText"
