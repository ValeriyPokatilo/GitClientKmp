package org.example.android.uikit.components.textField

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import org.example.android.uikit.R
import org.example.android.uikit.preview.MultiPreview
import org.example.android.uikit.preview.PreviewColumn
import org.example.android.uikit.theme.AppTheme

@Composable
fun DefaultTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    label: String? = null,
    placeholder: String? = null,
    errorText: String? = null,
    isError: Boolean = errorText != null,
    isValid: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions? = null,
    contentPadding: PaddingValues? = null,
    colors: TextFieldColors = AppTheme.componentColors.textFieldColors,
) {
    BaseTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        singleLine = singleLine,
        label = label,
        placeholder = placeholder,
        errorText = errorText,
        isError = isError,
        isValid = isValid,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        contentPadding = contentPadding,
        colors = colors,
    )
}

/**
 * All available colors for TextField:
 * textColor - focused, unfocused, disabled, error
 * containerColor - focused, unfocused, disabled, error
 * cursorColor - _, error
 * textSelectionColors
 * indicatorColor - focused, unfocused, disabled, error
 * leadingIconColor - focused, unfocused, disabled, error
 * trailingIconColor - focused, unfocused, disabled, error
 * labelColor - focused, unfocused, disabled, error
 * placeholderColor - focused, unfocused, disabled, error
 * supportingTextColor - focused, unfocused, disabled, error
 * prefixColor - focused, unfocused, disabled, error
 * suffixColor - focused, unfocused, disabled, error
 */
@Composable
internal fun defaultTextFieldColors() = OutlinedTextFieldDefaults.colors().copy(
    focusedTextColor = AppTheme.colors.onSurface,
    unfocusedTextColor = AppTheme.colors.onSurface,
    disabledTextColor = AppTheme.colors.onSurface.copy(alpha = 0.38f),
    errorTextColor = AppTheme.colors.onSurface,
    focusedContainerColor = AppTheme.colors.transparent,
    unfocusedContainerColor = AppTheme.colors.transparent,
    disabledContainerColor = AppTheme.colors.surfaceVariant,
    errorContainerColor = AppTheme.colors.transparent,
    cursorColor = AppTheme.colors.primary,
    errorCursorColor = AppTheme.colors.error,
    focusedIndicatorColor = AppTheme.colors.primary,
    unfocusedIndicatorColor = AppTheme.colors.onSurface,
    disabledIndicatorColor = AppTheme.colors.onSurface.copy(alpha = 0.38f),
    errorIndicatorColor = AppTheme.colors.error,
    focusedLeadingIconColor = AppTheme.colors.onSurfaceVariant,
    unfocusedLeadingIconColor = AppTheme.colors.onSurfaceVariant,
    disabledLeadingIconColor = AppTheme.colors.onSurface.copy(alpha = 0.38f),
    errorLeadingIconColor = AppTheme.colors.onSurfaceVariant,
    focusedTrailingIconColor = AppTheme.colors.onSurfaceVariant,
    unfocusedTrailingIconColor = AppTheme.colors.onSurfaceVariant,
    disabledTrailingIconColor = AppTheme.colors.onSurface.copy(alpha = 0.38f),
    errorTrailingIconColor = AppTheme.colors.error,
    focusedLabelColor = AppTheme.colors.onSurfaceVariant,
    unfocusedLabelColor = AppTheme.colors.onSurfaceVariant,
    disabledLabelColor = AppTheme.colors.onSurface.copy(alpha = 0.38f),
    errorLabelColor = AppTheme.colors.error,
    focusedPlaceholderColor = AppTheme.colors.onSurfaceVariant,
    unfocusedPlaceholderColor = AppTheme.colors.onSurfaceVariant,
    disabledPlaceholderColor = AppTheme.colors.onSurface.copy(alpha = 0.38f),
    errorPlaceholderColor = AppTheme.colors.onSurfaceVariant,
    errorSupportingTextColor = AppTheme.colors.error,
)

@MultiPreview
@Composable
private fun DefaultTextFieldPreview() = PreviewColumn {
    DefaultTextField(
        modifier = Modifier.fillMaxWidth(),
        value = "",
        onValueChange = {},
        label = "Название",
    )

    DefaultTextField(
        modifier = Modifier.fillMaxWidth(),
        value = "Какое-то очень длинное название в текстовом поле",
        onValueChange = {},
        label = "Название",
    )

    DefaultTextField(
        modifier = Modifier.fillMaxWidth(),
        value = "Какое-то очень длинное название в текстовом поле",
        onValueChange = {},
        label = "Название",
        errorText = "Некорректное название",
    )

    DefaultTextField(
        modifier = Modifier.fillMaxWidth(),
        value = "Какое-то очень длинное название в текстовом поле",
        onValueChange = {},
        label = "Название",
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_search),
                contentDescription = null
            )
        },
        trailingIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_close),
                contentDescription = null
            )
        },
    )
}
