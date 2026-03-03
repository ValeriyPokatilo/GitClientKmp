package org.example.android.uikit.components.pickerField

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import org.example.android.uikit.R
import org.example.android.uikit.preview.MultiPreview
import org.example.android.uikit.preview.PreviewColumn
import org.example.android.uikit.theme.AppTheme

@Composable
fun DefaultPickerField(
    value: String,
    onPickClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String? = null,
    placeholder: String? = null,
    errorText: String? = null,
    isError: Boolean = errorText != null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    interactionSource: MutableInteractionSource? = null,
    colors: PickerFieldColors = AppTheme.componentColors.pickerFieldColors,
) {
    BasePickerField(
        value = value,
        onClick = onPickClick,
        modifier = modifier,
        enabled = enabled,
        label = label,
        placeholder = placeholder,
        errorText = errorText,
        isError = isError,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        interactionSource = interactionSource,
        colors = colors,
    )
}

/**
 * All available colors for PickerField:
 * textColor - focused, unfocused, disabled, error
 * containerColor - focused, unfocused, disabled, error
 * indicatorColor - focused, unfocused, disabled, error
 * leadingIconColor - focused, unfocused, disabled, error
 * trailingIconColor - focused, unfocused, disabled, error
 * labelColor - focused, unfocused, disabled, error
 * placeholderColor - focused, unfocused, disabled, error
 * supportingTextColor - error (other states for supportingText are not implemented)
 */
@Composable
internal fun defaultPickerFieldColors(): PickerFieldColors = PickerFieldColors(
    focusedTextColor = AppTheme.colors.onSurface,
    unfocusedTextColor = AppTheme.colors.onSurface,
    disabledTextColor = AppTheme.colors.onSurface.copy(alpha = 0.38f),
    errorTextColor = AppTheme.colors.onSurface,
    focusedContainerColor = AppTheme.colors.transparent,
    unfocusedContainerColor = AppTheme.colors.transparent,
    disabledContainerColor = AppTheme.colors.transparent,
    errorContainerColor = AppTheme.colors.transparent,
    focusedIndicatorColor = AppTheme.colors.primary,
    unfocusedIndicatorColor = AppTheme.colors.outline,
    disabledIndicatorColor = AppTheme.colors.onSurface.copy(alpha = 0.12f),
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
private fun DefaultPickerFieldPreview() = PreviewColumn {
    DefaultPickerField(
        modifier = Modifier.fillMaxWidth(),
        label = "Название",
        value = "",
        onPickClick = {},
    )

    DefaultPickerField(
        modifier = Modifier.fillMaxWidth(),
        label = "Название",
        placeholder = "Выберите название",
        value = "",
        onPickClick = {},
    )

    DefaultPickerField(
        modifier = Modifier.fillMaxWidth(),
        label = "Название",
        value = "Какое-то название",
        onPickClick = {},
    )

    DefaultPickerField(
        modifier = Modifier.fillMaxWidth(),
        label = "Название",
        value = "Какое-то очень длинное название в текстовом поле",
        errorText = "Некорректное название",
        onPickClick = {},
    )

    DefaultPickerField(
        modifier = Modifier.fillMaxWidth(),
        label = "Название",
        value = "Какое-то очень длинное название в текстовом поле",
        onPickClick = {},
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_profile),
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
