package org.example.android.uikit.components.textField

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import org.example.android.uikit.R
import org.example.android.uikit.preview.MultiPreview
import org.example.android.uikit.preview.PreviewColumn
import org.example.android.uikit.theme.AppTheme

@Composable
fun EmailTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String? = null,
    placeholder: String? = null,
    errorText: String? = null,
    isError: Boolean = errorText != null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    colors: TextFieldColors = AppTheme.componentColors.textFieldColors,
) {
    BaseTextField(
        modifier = modifier
            .fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        label = label,
        placeholder = placeholder,
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_email),
                contentDescription = null
            )
        },
        errorText = errorText,
        isError = isError,
        keyboardOptions = keyboardOptions.copy(
            keyboardType = KeyboardType.Email,
        ),
        colors = colors,
    )
}

@MultiPreview
@Composable
private fun EmailTextFieldPreview() = PreviewColumn {
    EmailTextField(
        modifier = Modifier.fillMaxWidth(),
        value = "",
        onValueChange = {},
        label = "Email",
    )

    EmailTextField(
        modifier = Modifier.fillMaxWidth(),
        value = "",
        onValueChange = {},
        placeholder = "Введите email",
    )

    EmailTextField(
        modifier = Modifier.fillMaxWidth(),
        value = "email@example.com",
        onValueChange = {},
        label = "Email",
    )

    EmailTextField(
        modifier = Modifier.fillMaxWidth(),
        value = "email@example",
        onValueChange = {},
        label = "Email",
        errorText = "Некорректный email",
    )
}
