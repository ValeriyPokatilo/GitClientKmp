package org.example.android.uikit.components.dropDownMenu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.example.android.uikit.R
import org.example.android.uikit.preview.MultiPreview
import org.example.android.uikit.preview.PreviewBody
import org.example.android.uikit.theme.AppTheme

@Composable
fun <T> SelectableDropDownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    options: List<T>,
    optionName: (T) -> String,
    isSelected: (T) -> Boolean,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
    colors: DropDownMenuColors = AppTheme.componentColors.dropDownMenuColors,
) {
    DropdownMenu(
        modifier = modifier,
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        shape = AppTheme.shapes.s,
        containerColor = colors.containerColor,
        shadowElevation = 2.dp
    ) {
        SelectableDropDownMenuContent(
            modifier = contentModifier,
            options = options,
            optionName = optionName,
            isSelected = isSelected,
            onSelect = onSelect,
            colors = colors,
        )
    }
}

@Composable
fun <T> SelectableDropDownMenuContent(
    options: List<T>,
    optionName: (T) -> String,
    isSelected: (T) -> Boolean,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier,
    colors: DropDownMenuColors = AppTheme.componentColors.dropDownMenuColors,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
    ) {
        options.forEach {
            SelectableItem(
                name = optionName(it),
                isSelected = isSelected(it),
                onSelect = { onSelect(it) },
                colors = colors,
            )
        }
    }
}

@Composable
private fun SelectableItem(
    name: String,
    isSelected: Boolean?,
    onSelect: () -> Unit,
    colors: DropDownMenuColors,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clickable(onClick = onSelect),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (isSelected != null) {
            Icon(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .size(24.dp)
                    .alpha(if (isSelected) 1f else 0f),
                painter = painterResource(R.drawable.ic_checkmark),
                contentDescription = null,
                tint = colors.selectedContentColor
            )
        }

        Text(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp),
            text = name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = AppTheme.typography.title.medium,
            color = when (isSelected) {
                null -> colors.defaultContentColor
                false -> colors.defaultContentColor
                true -> colors.selectedContentColor
            }
        )
    }
}

@MultiPreview
@Composable
private fun SelectableDropDownMenuContentPreview() = PreviewBody {
    val options = listOf(
        1 to "Вариант 1",
        2 to "Вариант 2",
        3 to "Вариант 3",
        4 to "Вариант 4",
    )

    SelectableDropDownMenuContent(
        modifier = Modifier.background(AppTheme.componentColors.dropDownMenuColors.containerColor),
        options = options,
        optionName = { it.second },
        isSelected = { it.first == 2 },
        onSelect = {}
    )
}
