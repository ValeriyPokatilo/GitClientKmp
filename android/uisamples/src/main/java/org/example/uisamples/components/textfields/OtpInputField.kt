package org.example.uisamples.components.textfields

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import org.example.uisamples.themes.GgcTheme

@Composable
fun OtpInputField(
    modifier: Modifier = Modifier,
    length: Int = 6,
    text: MutableState<String>,
    error: String? = null,
    onComplete: (String) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        BasicTextField(
            value = text.value,
            onValueChange = { target ->
                if (target.length <= length) {
                    text.value = target
                }
                if (target.length == length) {
                    onComplete(target)
                }
            },
            modifier = Modifier.focusRequester(focusRequester),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            textStyle = TextStyle(color = Color.Transparent),
            decorationBox = { _ ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    repeat(length) { index ->
                        val char: String = when {
                            index >= text.value.length -> ""
                            else -> text.value[index].toString()
                        }
                        val isActive: Boolean = text.value.length == index
                        val isError: Boolean = error != null
                        val showBorder: Boolean = isActive || isError

                        OtpInputBox(
                            modifier = Modifier.weight(1f),
                            char = char,
                            showBorder = showBorder,
                            isError = isError,
                            isActive = isActive,
                        )
                    }
                }
            }
        )
        error?.let { OtpError(it) }
    }
}

@Composable
private fun OtpInputBox(
    modifier: Modifier = Modifier,
    char: String,
    showBorder: Boolean,
    isError: Boolean,
    isActive: Boolean,
) {
    Box(
        modifier = modifier
            .heightIn(min = 56.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .border(
                width = if (showBorder) 2.dp else 0.dp,
                color = if (showBorder) otpInputBorderColor(isError) else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = char,
            style = GgcTheme.typography.inputContent.copy(
                color = MaterialTheme.colorScheme.onSurface
            )
        )
        if (isActive) {
            OtpInputCursor()
        }
    }
}

@Composable
private fun OtpError(error: String) {
    Text(
        text = error,
        style = MaterialTheme.typography.bodySmall.copy(
            color = MaterialTheme.colorScheme.error
        )
    )
}

@Composable
private fun OtpInputCursor() {
    Box(
        modifier = Modifier
            .height(18.dp)
            .width(1.dp)
            .background(MaterialTheme.colorScheme.primary)
    )
}

@Composable
private fun otpInputBorderColor(isError: Boolean): Color {
    return if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
}

@PreviewLightDark
@Composable
private fun OtpInputFieldPreview() {
    val value: MutableState<String> = remember { mutableStateOf("") }
    var error: String? by remember { mutableStateOf(null) }

    OtpInputField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        length = 5,
        text = value,
        error = error,
        onComplete = { error = "Invalid code" }
    )
}
