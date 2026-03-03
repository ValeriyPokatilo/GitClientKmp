package org.example.uisamples.components.textfields

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewLightDark
import org.example.uisamples.components.textfields.visualtransformation.CardNumberVisualTransformation
import org.example.uisamples.themes.BizonTheme

private const val CARD_LIMIT = 16

@Composable
fun BizonCardNumberField(
    modifier: Modifier = Modifier,
    value: String,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
    isSingleLine: Boolean,
    errorText: String?,
    onChange: (String) -> Unit,
    label: String
) {
    BizonTextField(
        modifier = modifier,
        value = value,
        keyboardType = keyboardType,
        visualTransformation = CardNumberVisualTransformation(
            mask = "0000 0000 0000 0000",
            maskNumber = '0',
        ),
        imeAction = imeAction,
        spacing = HintSpacing.Small,
        isSingleLine = isSingleLine,
        errorText = errorText,
        onChange = { number ->
            if (number.all { it.isDigit() }) {
                val newValue = number.replace(" ", "")
                if (newValue.length <= CARD_LIMIT) {
                    onChange(newValue)
                }
            }
        },
        label = label
    )
}

@PreviewLightDark
@Composable
private fun BizonCardNumberFieldPreview() {
    BizonTheme(isSystemInDarkTheme()) {
        val cardNumber = remember { mutableStateOf("1234567812345678") }
        BizonCardNumberField(
            modifier = Modifier,
            value = cardNumber.value,
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next,
            isSingleLine = true,
            errorText = null,
            label = "Номер карты",
            onChange = { cardNumber.value = it },
        )
    }
}
