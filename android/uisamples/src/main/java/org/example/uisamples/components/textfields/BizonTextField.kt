package org.example.uisamples.components.textfields

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.example.android.uisamples.R
import org.example.uisamples.themes.B
import org.example.uisamples.themes.BizonTheme

@Composable
fun BizonTextField(
    modifier: Modifier = Modifier,
    hintText: String,
    text: String = "",
    textMaxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    onClick: (() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .border(
                width = 1.dp,
                color = B.colors.border90,
                shape = RoundedCornerShape(10.dp)
            )
            .background(Color.Transparent)
            .clip(shape = RoundedCornerShape(10.dp))
            .clickable { onClick?.invoke() }
    ) {
        if (text.isEmpty()) {
            Text(
                text = hintText,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp),
                style = B.typography.mobile.text.hint,
                color = B.colors.secondary
            )
        } else {
            Column {
                Text(
                    modifier = Modifier.padding(
                        start = 20.dp,
                        end = 20.dp,
                        top = 10.dp,
                        bottom = 0.dp
                    ),
                    text = hintText,
                    style = B.typography.mobile.text.smallHint,
                    color = B.colors.secondary
                )

                Text(
                    modifier = Modifier.padding(
                        start = 20.dp,
                        end = 20.dp,
                        top = 0.dp,
                        bottom = 10.dp
                    ),
                    text = text,
                    maxLines = textMaxLines,
                    overflow = overflow,
                    style = B.typography.mobile.text.label,
                    color = B.colors.primary
                )
            }
        }
    }
}

@Suppress("LongMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BizonTextField(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    isEditable: Boolean = true,
    isSingleLine: Boolean = false,
    errorText: String? = null,
    spacing: HintSpacing = HintSpacing.Large,
    minHeight: Dp = 56.dp,
    maxHeight: Dp = Dp.Unspecified,
    imeAction: ImeAction = ImeAction.Next,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onChange: (String) -> Unit,
    onClick: () -> Unit = {},
    textSelection: Int = 0
) {
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused = interactionSource.collectIsFocusedAsState()

    val colors = OutlinedTextFieldDefaults.colors(
        disabledContainerColor = B.colors.white,
        unfocusedContainerColor = B.colors.white,
        cursorColor = B.colors.accent,
        focusedBorderColor = B.colors.accent,
        unfocusedBorderColor = B.colors.transparent,
        unfocusedPlaceholderColor = B.colors.white,
        focusedPlaceholderColor = B.colors.white,
        unfocusedTextColor = B.colors.primary,
        focusedTextColor = B.colors.primary,
        focusedLabelColor = B.colors.secondary,
        unfocusedLabelColor = B.colors.secondary
    )
    val customTextSelectionColors = TextSelectionColors(
        handleColor = B.colors.primaryRed,
        backgroundColor = B.colors.primaryRed
    )

    var textFieldValue = TextFieldValue(
        text = value,
        selection = TextRange(textSelection)
    )

    CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
        Column(modifier = modifier) {
            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = minHeight, max = maxHeight)
                    .border(
                        width = 1.dp,
                        color = when {
                            errorText != null -> B.colors.systemError
                            isFocused.value -> B.colors.accent
                            else -> B.colors.border90
                        },
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clip(RoundedCornerShape(10.dp))
                    .clickable(onClick = onClick),
                value = textFieldValue.text,
                onValueChange = {
                    textFieldValue = TextFieldValue(text = it)
                    onChange(textFieldValue.text)
                },
                readOnly = !isEditable,
                enabled = isEditable,
                textStyle = B.typography.primary.small,
                cursorBrush = SolidColor(B.colors.borderColor),
                decorationBox = @Composable { innerTextField ->
                    OutlinedTextFieldDefaults.DecorationBox(
                        value = value,
                        innerTextField = innerTextField,
                        enabled = false,
                        isError = errorText != null,
                        singleLine = false,
                        visualTransformation = VisualTransformation.None,
                        interactionSource = interactionSource,
                        label = {
                            Text(
                                modifier = Modifier.padding(
                                    top = if (!isFocused.value && value.isNotEmpty() || isFocused.value) {
                                        spacing.unfocusedTopPadding
                                    } else {
                                        spacing.focusedTopPadding
                                    }
                                ),
                                text = label,
                                color = when {
                                    errorText != null -> B.colors.systemError
                                    isFocused.value -> B.colors.accent
                                    else -> B.colors.secondary
                                },
                                style = if (!isFocused.value && value.isNotEmpty() || isFocused.value) {
                                    B.typography.mobile.text.smallHint.copy(
                                        fontFamily = FontFamily(Font(R.font.sf_pro))
                                    )
                                } else {
                                    B.typography.secondary.small
                                }
                            )
                        },
                        contentPadding = OutlinedTextFieldDefaults.contentPadding(
                            start = 20.dp,
                            top = 0.dp,
                            end = 20.dp,
                            bottom = 10.dp
                        ),
                        colors = colors
                    )
                },
                interactionSource = interactionSource,
                keyboardOptions = KeyboardOptions(
                    imeAction = imeAction,
                    keyboardType = keyboardType
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    },
                    onDone = {
                        focusManager.moveFocus(FocusDirection.Down)
                        focusManager.clearFocus()
                    }
                ),
                singleLine = isSingleLine,
                visualTransformation = visualTransformation
            )

            if (errorText != null) {
                Text(
                    modifier = Modifier.padding(
                        top = 4.dp,
                        start = 20.dp,
                        end = 20.dp
                    ),
                    text = errorText,
                    overflow = TextOverflow.Ellipsis,
                    color = B.colors.systemError,
                    style = B.typography.mobile.m3.bodySmall
                )
            }
        }
    }
}

sealed class HintSpacing(
    val focusedTopPadding: Dp,
    val unfocusedTopPadding: Dp
) {

    data object Large : HintSpacing(
        focusedTopPadding = 16.dp,
        unfocusedTopPadding = 36.dp
    )

    data object LargeAddress : HintSpacing(
        focusedTopPadding = 20.dp,
        unfocusedTopPadding = 36.dp
    )

    data object Small : HintSpacing(
        //currently used for NewCardContent & RegistrationContent, & the focused variant only
        focusedTopPadding = 18.dp,
        unfocusedTopPadding = 28.dp
    )
}

@PreviewLightDark
@Composable
private fun BizonTextFieldPreview() {
    BizonTheme(isSystemInDarkTheme()) {
        BizonTextField(
            modifier = Modifier,
            hintText = "hintText",
            text = "text",
            textMaxLines = 1,
            onClick = {},
        )
    }
}
