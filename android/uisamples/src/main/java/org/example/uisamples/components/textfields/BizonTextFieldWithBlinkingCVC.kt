package org.example.uisamples.components.textfields

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewLightDark
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.uisamples.components.textfields.visualtransformation.BlinkingVisualTransformation
import org.example.uisamples.themes.BizonTheme

private const val BLINK_TIMEOUT = 1000L

@Composable
fun BizonTextFieldWithBlinkingCVC(
    modifier: Modifier = Modifier,
    value: String,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
    isSingleLine: Boolean,
    errorText: String?,
    onChange: (String) -> Unit,
    label: String
) {
    var showLastChar by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = value) {
        if (value.isNotEmpty()) {
            showLastChar = true
            coroutineScope.launch {
                delay(BLINK_TIMEOUT)
                showLastChar = false
            }
        }
    }

    BizonTextField(
        modifier = modifier,
        value = value,
        keyboardType = keyboardType,
        visualTransformation = BlinkingVisualTransformation(showLastChar),
        imeAction = imeAction,
        spacing = HintSpacing.Small,
        isSingleLine = isSingleLine,
        errorText = errorText,
        onChange = onChange,
        label = label
    )
}

@PreviewLightDark
@Composable
private fun BizonTextFieldWithBlinkingCVCPreview() {
    BizonTheme(isSystemInDarkTheme()) {
        val cvcNumber = remember { mutableStateOf("123") }
        BizonTextFieldWithBlinkingCVC(
            modifier = Modifier,
            value = cvcNumber.value,
            keyboardType = KeyboardType.Number,
            isSingleLine = false,
            errorText = null,
            imeAction = ImeAction.Next,
            onChange = { cvcNumber.value = it },
            label = "cvc",
        )
    }
}
