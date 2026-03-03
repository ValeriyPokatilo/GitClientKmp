package org.example.android.uikit.components.pickerField

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.example.android.uikit.R
import org.example.android.uikit.preview.MultiPreview
import org.example.android.uikit.preview.PreviewColumn
import org.example.android.uikit.theme.AppTheme

@Composable
fun <T> DropDownPickerField(
    value: String,
    options: List<T>,
    optionName: (T) -> String,
    isSelected: (T) -> Boolean,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String? = null,
    placeholder: String = "...",
    errorText: String? = null,
    isError: Boolean = errorText != null,
    isValid: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    interactionSource: MutableInteractionSource? = null,
    colors: PickerFieldColors = AppTheme.componentColors.pickerFieldColors,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        var isDropdownMenuOpen: Boolean by remember { mutableStateOf(false) }

        BasePickerField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onClick = { isDropdownMenuOpen = !isDropdownMenuOpen },
            enabled = enabled,
            label = label,
            placeholder = placeholder,
            errorText = errorText,
            isError = isError,
            isValid = isValid,
            leadingIcon = leadingIcon,
            trailingIcon = {
                Icon(
                    painter = when (isDropdownMenuOpen) {
                        true -> painterResource(R.drawable.ic_arrow_up)
                        false -> painterResource(R.drawable.ic_arrow_down)
                    },
                    contentDescription = null
                )
            },
            interactionSource = interactionSource,
            colors = colors,
        )

        DropdownMenu(
            modifier = Modifier.align(Alignment.TopEnd),
            expanded = isDropdownMenuOpen,
            onDismissRequest = { isDropdownMenuOpen = false },
            shape = AppTheme.shapes.s,
            containerColor = AppTheme.colors.surfaceContainerLow,
            shadowElevation = 2.dp
        ) {
            DropDownMenuContent(
                options = options,
                optionName = optionName,
                isSelected = isSelected,
                onSelect = {
                    onSelect(it)
                    isDropdownMenuOpen = false
                },
            )
        }
    }
}

@Composable
private fun <T> DropDownMenuContent(
    options: List<T>,
    optionName: (T) -> String,
    isSelected: (T) -> Boolean,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .heightIn(max = 320.dp)
            .width(182.dp)
            .verticalScroll(rememberScrollState())
    ) {
        options.forEach { option ->
            CheckmarkItem(
                name = optionName(option),
                isSelected = isSelected(option),
                onSelect = { onSelect(option) }
            )
        }
    }
}

@Composable
private fun CheckmarkItem(
    name: String,
    isSelected: Boolean,
    onSelect: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clickable(onClick = onSelect),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .size(24.dp)
                .alpha(if (isSelected) 1f else 0f),
            painter = painterResource(R.drawable.ic_checkmark),
            contentDescription = null,
            tint = AppTheme.colors.onSurface
        )

        Text(
            modifier = Modifier.weight(1f),
            text = name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = AppTheme.colors.onSurface,
            style = AppTheme.typography.title.medium
        )
    }
}

@MultiPreview
@Composable
private fun DropDownPickerFieldPreview() = PreviewColumn {
    val options = listOf("руб", "дол", "евр")

    DropDownPickerField(
        modifier = Modifier.fillMaxWidth(),
        label = "Валюта",
        placeholder = "Выберите валюту",
        value = "",
        options = options,
        optionName = { it },
        isSelected = { it == "руб" },
        onSelect = {},
    )

    DropDownPickerField(
        modifier = Modifier.fillMaxWidth(),
        label = "Валюта",
        value = "руб",
        options = options,
        optionName = { it },
        isSelected = { it == "руб" },
        onSelect = {},
    )

    DropDownPickerField(
        modifier = Modifier.fillMaxWidth(),
        value = "",
        options = options,
        optionName = { it },
        isSelected = { it == "руб" },
        onSelect = {},
    )

    DropDownPickerField(
        modifier = Modifier.fillMaxWidth(),
        value = "руб",
        options = options,
        optionName = { it },
        isSelected = { it == "руб" },
        onSelect = {},
    )

    DropDownMenuContent(
        modifier = Modifier.background(AppTheme.colors.surfaceContainerLow),
        options = options,
        optionName = { it },
        isSelected = { it == "руб" },
        onSelect = {},
    )
}
