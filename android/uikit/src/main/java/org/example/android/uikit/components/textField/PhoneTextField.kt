package org.example.android.uikit.components.textField

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import org.example.android.uikit.R
import org.example.android.uikit.preview.MultiPreview
import org.example.android.uikit.preview.PreviewColumn
import org.example.android.utils.PhoneVisualTransformation

@Composable
fun PhoneTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String? = null,
    errorText: String? = null,
    isError: Boolean = errorText != null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    BaseTextField(
        modifier = modifier
            .fillMaxWidth(),
        value = value,
        onValueChange = { newValue ->
            if (
                newValue.length <= MAX_PHONE_LENGTH &&
                newValue.all { char -> char.isDigit() }
            ) {
                onValueChange(newValue)
            }
        },
        enabled = enabled,
        label = label,
        placeholder = PLACEHOLDER_PHONE,
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_phone),
                contentDescription = null
            )
        },
        errorText = errorText,
        isError = isError,
        visualTransformation = remember { PhoneVisualTransformation() },
        keyboardOptions = keyboardOptions.copy(
            keyboardType = KeyboardType.Number,
        ),
    )
}

private const val MAX_PHONE_LENGTH = 10
private const val PLACEHOLDER_PHONE = "+7 (000) 000-00-00"

@MultiPreview
@Composable
private fun PhoneTextFieldPreview() = PreviewColumn {
    PhoneTextField(
        modifier = Modifier.fillMaxWidth(),
        value = "",
        onValueChange = {},
        label = "Номер телефона",
    )

    PhoneTextField(
        modifier = Modifier.fillMaxWidth(),
        value = "9006004",
        onValueChange = {},
        label = "Номер телефона",
    )

    PhoneTextField(
        modifier = Modifier.fillMaxWidth(),
        value = "9006004321",
        onValueChange = {},
        label = "Номер телефона",
    )

    PhoneTextField(
        modifier = Modifier.fillMaxWidth(),
        value = "9006004321",
        onValueChange = {},
        label = "Номер телефона",
        errorText = "Неизвестный номер",
    )
}
