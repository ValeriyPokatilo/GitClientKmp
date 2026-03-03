package org.example.uisamples.components.textfields

import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.example.uisamples.themes.B
import org.example.uisamples.themes.BizonTheme

@Suppress("LongMethod")
@Composable
fun BizonInputTextField(
    modifier: Modifier = Modifier,
    text: String = "",
    hintText: String,
    onTextChange: (String) -> Unit,
    singleLine: Boolean = false,
    inputAlignment: Alignment = Alignment.Center,
    inputTopPadding: Dp = 12.dp,
    trailingIcon: @Composable (() -> Unit)? = null,
    mainHint: @Composable (BoxScope.() -> Unit)? = null,
    smallHint: @Composable (BoxScope.() -> Unit)? = null,
    height: Dp = 56.dp,
    textSelection: Int = 0,
    errorText: String? = null
) {
    var isFocused by remember { mutableStateOf(false) }

    val borderColor = when {
        isFocused && errorText == null -> B.colors.accent
        isFocused && errorText != null -> B.colors.systemError
        else -> B.colors.border90
    }

    val labelColor = if (isFocused) {
        B.colors.accent
    } else {
        B.colors.secondary
    }

    Box(
        modifier = modifier
            .heightIn(min = height)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(10.dp)
            )
    ) {

        val customTextSelectionColors = TextSelectionColors(
            handleColor = B.colors.primaryRed,
            backgroundColor = B.colors.greySemiAlpha
        )

        var textFieldValue by remember {
            mutableStateOf(
                TextFieldValue(
                    text = text,
                    selection = TextRange(textSelection)
                )
            )
        }

        CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
            BasicTextField(
                modifier = Modifier
                    .align(inputAlignment)
                    .padding(
                        start = 20.dp,
                        top = inputTopPadding,
                        end = 8.dp
                    )
                    .fillMaxWidth()
                    .onFocusChanged {
                        isFocused = it.isFocused
                    },
                value = textFieldValue,
                singleLine = singleLine,
                textStyle = B.typography.mobile.text.hint,
                onValueChange = {
                    textFieldValue = it
                    onTextChange(textFieldValue.text)
                }
            )
        }

        if (!isFocused && text.isEmpty()) {
            if (mainHint != null) {
                mainHint()
            } else {
                Text(
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .align(Alignment.CenterStart),
                    text = hintText,
                    style = B.typography.mobile.text.hint,
                    color = labelColor
                )
            }
        } else {
            if (smallHint != null) {
                smallHint()
            } else {
                Text(
                    modifier = Modifier
                        .padding(start = 20.dp, top = 10.dp)
                        .align(Alignment.TopStart),
                    text = hintText,
                    style = B.typography.mobile.text.smallHint,
                    color = labelColor
                )
            }
        }

        trailingIcon?.let {
            Box(
                modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                trailingIcon()
            }
        }
    }
    if (errorText != null) {
        Text(
            modifier = Modifier.padding(
                top = 4.dp,
                start = 36.dp,
                end = 20.dp
            ),
            text = errorText,
            overflow = TextOverflow.Ellipsis,
            color = B.colors.systemError,
            style = B.typography.mobile.m3.bodySmall
        )
    }
}

@PreviewLightDark
@Composable
private fun BizonInputTextFieldPreview() {
    BizonTheme(isSystemInDarkTheme()) {
        BizonInputTextField(
            modifier = Modifier,
            text = "text",
            hintText = "hintText",
            onTextChange = {},
            singleLine = true,
        )
    }
}
