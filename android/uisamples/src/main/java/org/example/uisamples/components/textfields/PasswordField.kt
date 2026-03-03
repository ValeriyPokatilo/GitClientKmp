package org.example.uisamples.components.textfields

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.PreviewLightDark
import org.example.android.uisamples.R
import org.example.uisamples.themes.GgcTheme

@Composable
fun PasswordField(
    label: String,
    text: MutableState<String>,
    modifier: Modifier = Modifier,
    error: String? = null,
    enabled: Boolean = true,
) {
    var isPasswordVisible: Boolean by remember { mutableStateOf(false) }

    GgcTextField(
        modifier = modifier,
        value = text.value,
        onValueChange = { text.value = it },
        singleLine = true,
        error = error,
        leadingIconPainter = painterResource(R.drawable.ic_lock),
        trailingIconPainter = painterResource(
            if (isPasswordVisible) R.drawable.ic_eye_hide else R.drawable.ic_eye_show
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        label = { Text(text = label) },
        onTrailingButtonClick = { isPasswordVisible = !isPasswordVisible },
        enabled = enabled
    )
}

@PreviewLightDark
@Composable
private fun PasswordFieldPreview() {
    val text: MutableState<String> = remember { mutableStateOf("value") }
    GgcTheme {
        PasswordField(
            modifier = Modifier,
            text = text,
            label = "label"
        )
    }
}
