package org.example.android.uikit.components.textField

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults.FocusedBorderThickness
import androidx.compose.material3.OutlinedTextFieldDefaults.UnfocusedBorderThickness
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.example.android.uikit.theme.AppTheme

@Suppress("LongMethod", "CyclomaticComplexMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BaseTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    label: String? = null,
    placeholder: String? = null,
    errorText: String? = null,
    isError: Boolean = errorText != null,
    isValid: Boolean = false,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    container: @Composable (() -> Unit)? = null,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions? = null,
    contentPadding: PaddingValues? = null,
    textStyle: TextStyle = AppTheme.typography.label.large,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
) {
    val focusManager = LocalFocusManager.current
    val isFocused = interactionSource.collectIsFocusedAsState().value

    val textColor = when {
        !enabled -> colors.disabledTextColor
        isError -> colors.errorTextColor
        isFocused -> colors.focusedTextColor
        else -> colors.unfocusedTextColor
    }
    val mergedTextStyle = textStyle.merge(TextStyle(color = textColor))

    val labelStyle = if (value.isNotEmpty() || isFocused) {
        AppTheme.typography.body.small
    } else {
        AppTheme.typography.body.large
    }

    val cursorColor = when {
        isError -> colors.errorCursorColor
        else -> colors.cursorColor
    }

    CompositionLocalProvider(LocalTextSelectionColors provides colors.textSelectionColors) {
        BasicTextField(
            modifier = modifier
                .then(
                    if (label != null) {
                        Modifier
                            // Merge semantics at the beginning of the modifier chain to ensure
                            // padding is considered part of the text field.
                            .semantics(mergeDescendants = true) {}
                            .padding(top = 8.dp)
                    } else {
                        Modifier
                    }
                )
                .then(
                    if (isError) {
                        Modifier.semantics { error(errorText.orEmpty()) }
                    } else {
                        Modifier
                    }
                )
                .defaultMinSize(
                    minWidth = OutlinedTextFieldDefaults.MinWidth,
                    minHeight = OutlinedTextFieldDefaults.MinHeight
                ),
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            readOnly = false,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            textStyle = mergedTextStyle,
            cursorBrush = SolidColor(cursorColor),
            onTextLayout = onTextLayout,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions ?: KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
            interactionSource = interactionSource,
            decorationBox = @Composable { innerTextField ->
                OutlinedTextFieldDefaults.DecorationBox(
                    value = value,
                    visualTransformation = visualTransformation,
                    innerTextField = innerTextField,
                    label = label?.ifEmpty { null }?.let {
                        {
                            Text(
                                text = label,
                                maxLines = 1,
                                style = labelStyle,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    },
                    placeholder = placeholder?.let {
                        {
                            Text(
                                text = placeholder,
                                style = AppTheme.typography.label.large
                            )
                        }
                    },
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon,
                    supportingText = errorText?.let {
                        {
                            Text(
                                text = errorText,
                                style = AppTheme.typography.label.small,
                                color = colors.errorSupportingTextColor
                            )
                        }
                    },
                    singleLine = singleLine,
                    enabled = enabled,
                    isError = isError,
                    interactionSource = interactionSource,
                    colors = colors,
                    contentPadding = contentPadding ?: OutlinedTextFieldDefaults.contentPadding(),
                    container = container ?: {
                        ContainerBox(
                            enabled = enabled,
                            isError = isError,
                            isValid = isValid,
                            interactionSource = interactionSource,
                            colors = colors,
                            shape = AppTheme.shapes.m,
                        )
                    }
                )
            }
        )
    }
}

@ExperimentalMaterial3Api
@Composable
private fun ContainerBox(
    enabled: Boolean,
    isError: Boolean,
    isValid: Boolean,
    interactionSource: InteractionSource,
    colors: TextFieldColors,
    shape: Shape,
    focusedBorderThickness: Dp = FocusedBorderThickness,
    unfocusedBorderThickness: Dp = UnfocusedBorderThickness
) {
    val borderStroke: BorderStroke by animateBorderStrokeAsState(
        enabled = enabled,
        isError = isError,
        isValid = isValid,
        interactionSource = interactionSource,
        colors = colors,
        focusedBorderThickness = focusedBorderThickness,
        unfocusedBorderThickness = unfocusedBorderThickness
    )
    val containerColor: Color by colors.containerColor(
        enabled = enabled,
        isError = isError,
        isValid = isValid,
        interactionSource = interactionSource
    )
    Box(
        Modifier
            .border(borderStroke, shape)
            .background(containerColor, shape)
    )
}

@Composable
private fun TextFieldColors.containerColor(
    enabled: Boolean,
    isError: Boolean,
    isValid: Boolean,
    interactionSource: InteractionSource
): State<Color> {
    val focused: Boolean = interactionSource.collectIsFocusedAsState().value

    val targetValue: Color = when {
        !enabled -> disabledContainerColor
        isError -> errorContainerColor
        isValid -> focusedContainerColor
        focused -> focusedContainerColor
        else -> unfocusedContainerColor
    }

    return animateColorAsState(
        targetValue = targetValue,
        animationSpec = tween(durationMillis = ANIMATION_DURATION)
    )
}

@Composable
private fun animateBorderStrokeAsState(
    enabled: Boolean,
    isError: Boolean,
    isValid: Boolean,
    interactionSource: InteractionSource,
    colors: TextFieldColors,
    focusedBorderThickness: Dp,
    unfocusedBorderThickness: Dp
): State<BorderStroke> {
    val focused: Boolean by interactionSource.collectIsFocusedAsState()
    val indicatorColor: State<Color> = colors.indicatorColor(
        enabled = enabled,
        isError = isError,
        isValid = isValid,
        interactionSource = interactionSource
    )
    val targetThickness: Dp = when {
        focused -> focusedBorderThickness
        else -> unfocusedBorderThickness
    }

    val animatedThickness = if (enabled) {
        animateDpAsState(
            targetValue = targetThickness,
            animationSpec = tween(durationMillis = ANIMATION_DURATION)
        )
    } else {
        rememberUpdatedState(unfocusedBorderThickness)
    }

    return rememberUpdatedState(
        BorderStroke(
            width = animatedThickness.value,
            brush = SolidColor(indicatorColor.value)
        )
    )
}

@Composable
internal fun TextFieldColors.indicatorColor(
    enabled: Boolean,
    isError: Boolean,
    isValid: Boolean,
    interactionSource: InteractionSource
): State<Color> {
    val focused: Boolean by interactionSource.collectIsFocusedAsState()

    val targetValue: Color = when {
        !enabled -> disabledIndicatorColor
        isError -> errorIndicatorColor
        isValid -> focusedIndicatorColor
        focused -> focusedIndicatorColor
        else -> unfocusedIndicatorColor
    }
    return if (enabled) {
        animateColorAsState(
            targetValue = targetValue,
            animationSpec = tween(durationMillis = ANIMATION_DURATION)
        )
    } else {
        rememberUpdatedState(targetValue)
    }
}

private const val ANIMATION_DURATION = 150
