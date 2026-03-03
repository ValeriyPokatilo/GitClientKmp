package org.example.android.uikit.components.textField

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import org.example.android.uikit.R
import org.example.android.uikit.preview.MultiPreview
import org.example.android.uikit.preview.PreviewColumn
import org.example.android.uikit.theme.AppTheme

@Composable
fun PasswordTextField(
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
    var isPasswordVisible by remember { mutableStateOf(false) }

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
                painter = painterResource(R.drawable.ic_password),
                contentDescription = null
            )
        },
        trailingIcon = {
            val iconResId = when (isPasswordVisible) {
                true -> R.drawable.ic_password_hide
                false -> R.drawable.ic_password_show
            }

            Icon(
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { isPasswordVisible = !isPasswordVisible },
                ),
                painter = painterResource(iconResId),
                contentDescription = null
            )
        },
        errorText = errorText,
        isError = isError,
        visualTransformation = when (isPasswordVisible) {
            true -> VisualTransformation.None
            false -> PasswordVisualTransformation()
        },
        keyboardOptions = keyboardOptions.copy(
            keyboardType = KeyboardType.Password,
        ),
        colors = colors,
    )
}

@MultiPreview
@Composable
private fun PasswordTextFieldPreview() = PreviewColumn {
    PasswordTextField(
        modifier = Modifier.fillMaxWidth(),
        value = "",
        onValueChange = {},
        label = "Пароль",
    )

    PasswordTextField(
        modifier = Modifier.fillMaxWidth(),
        value = "",
        onValueChange = {},
        placeholder = "Введите пароль",
    )

    PasswordTextField(
        modifier = Modifier.fillMaxWidth(),
        value = "password1234",
        onValueChange = {},
        label = "Пароль",
    )

    PasswordTextField(
        modifier = Modifier.fillMaxWidth(),
        value = "longLongLongLongPassword1234",
        onValueChange = {},
        label = "Пароль",
        errorText = "Слишком длинный пароль",
    )
}
