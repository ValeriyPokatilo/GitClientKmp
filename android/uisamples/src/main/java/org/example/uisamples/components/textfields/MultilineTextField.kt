package org.example.uisamples.components.textfields

import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import org.example.uisamples.themes.GgcTheme

@Composable
fun MultilineTextField(
    modifier: Modifier = Modifier,
    text: MutableState<String>,
    maxLines: Int = 6,
    minLines: Int = 2,
    placeholder: String? = null,
    enabled: Boolean = true,
    error: String? = null,
) {
    GgcTextField(
        modifier = modifier,
        value = text.value,
        onValueChange = { text.value = it },
        maxLines = maxLines,
        minLines = minLines,
        error = error,
        enabled = enabled,
        placeholder = { placeholder?.let { Text(text = it) } },
        colors = GgcTheme.textFieldColors.copy(
            focusedPlaceholderColor = colorScheme.onSurface,
            unfocusedPlaceholderColor = colorScheme.onSurface,
            errorPlaceholderColor = colorScheme.onSurface
        )
    )
}

@PreviewLightDark
@Composable
private fun MultilineTextFieldPreview() {
    val text: MutableState<String> = remember { mutableStateOf("value") }
    GgcTheme {
        MultilineTextField(
            modifier = Modifier,
            text = text,
        )
    }
}
