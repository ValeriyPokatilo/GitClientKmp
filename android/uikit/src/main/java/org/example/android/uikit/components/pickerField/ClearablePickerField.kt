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
import org.example.android.utils.clickableRipple

@Composable
fun ClearablePickerField(
    value: String,
    onPickClick: () -> Unit,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String? = null,
    placeholder: String? = null,
    errorText: String? = null,
    isError: Boolean = errorText != null,
    leadingIcon: @Composable (() -> Unit)? = null,
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
        trailingIcon = {
            if (value.isEmpty()) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_right),
                    contentDescription = null
                )
            } else {
                Icon(
                    modifier = Modifier.clickableRipple(
                        onClick = onClearClick
                    ),
                    painter = painterResource(R.drawable.ic_clear),
                    contentDescription = null
                )
            }
        },
        interactionSource = interactionSource,
        colors = colors,
    )
}

@MultiPreview
@Composable
private fun ClearablePickerFieldPreview() = PreviewColumn {
    ClearablePickerField(
        modifier = Modifier.fillMaxWidth(),
        label = "Название",
        value = "",
        onPickClick = {},
        onClearClick = {},
    )

    ClearablePickerField(
        modifier = Modifier.fillMaxWidth(),
        label = "Название",
        placeholder = "Выберите название",
        value = "",
        onPickClick = {},
        onClearClick = {},
    )

    ClearablePickerField(
        modifier = Modifier.fillMaxWidth(),
        label = "Название",
        value = "Какое-то название",
        onPickClick = {},
        onClearClick = {},
    )

    ClearablePickerField(
        modifier = Modifier.fillMaxWidth(),
        label = "Название",
        value = "Какое-то очень длинное название в текстовом поле",
        errorText = "Некорректное название",
        onPickClick = {},
        onClearClick = {},
    )

    ClearablePickerField(
        modifier = Modifier.fillMaxWidth(),
        label = "Название",
        value = "Какое-то очень длинное название в текстовом поле",
        onPickClick = {},
        onClearClick = {},
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_profile),
                contentDescription = null
            )
        },
    )
}
