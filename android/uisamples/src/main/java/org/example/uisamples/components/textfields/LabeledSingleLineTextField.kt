package org.example.uisamples.components.textfields

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.PreviewLightDark
import org.example.uisamples.themes.GgcTheme

@Composable
fun LabeledSingleLineTextField(
    modifier: Modifier = Modifier,
    text: MutableState<String>,
    label: String,
    error: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    enabled: Boolean = true,
    maxLength: Int? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    GgcTextField(
        modifier = modifier,
        value = text.value,
        onValueChange = {
            if (maxLength == null || maxLength >= it.length) {
                text.value = it
            } else {
                text.value = it.substring(0, maxLength)
            }
        },
        singleLine = true,
        error = error,
        enabled = enabled,
        label = { Text(text = label) },
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions
    )
}

@PreviewLightDark
@Composable
private fun LabeledSingleLineTextFieldPreview() {
    val text: MutableState<String> = remember { mutableStateOf("value") }
    GgcTheme {
        LabeledSingleLineTextField(
            modifier = Modifier,
            text = text,
            label = "label"
        )
    }
}
