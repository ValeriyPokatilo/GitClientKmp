package org.example.android.uikit.components.dropDownMenu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.example.android.uikit.preview.MultiPreview
import org.example.android.uikit.preview.PreviewBody
import org.example.android.uikit.theme.AppTheme

@Composable
fun <T> DefaultDropDownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    options: List<T>,
    optionName: (T) -> String,
    onOptionClick: (T) -> Unit,
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
        DefaultDropDownMenuContent(
            modifier = contentModifier,
            options = options,
            optionName = optionName,
            onOptionClick = onOptionClick,
            colors = colors,
        )
    }
}

@Composable
fun <T> DefaultDropDownMenuContent(
    options: List<T>,
    optionName: (T) -> String,
    onOptionClick: (T) -> Unit,
    modifier: Modifier = Modifier,
    colors: DropDownMenuColors = AppTheme.componentColors.dropDownMenuColors,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
    ) {
        options.forEach {
            TextItem(
                name = optionName(it),
                onClick = { onOptionClick(it) },
                colors = colors,
            )
        }
    }
}

@Composable
private fun TextItem(
    name: String,
    onClick: () -> Unit,
    colors: DropDownMenuColors,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = AppTheme.typography.title.medium,
            color = colors.defaultContentColor,
        )
    }
}

@Composable
fun defaultDropDownMenuColors(): DropDownMenuColors = DropDownMenuColors(
    containerColor = AppTheme.colors.surfaceContainerLow,
    defaultContentColor = AppTheme.colors.onSurface,
    selectedContentColor = AppTheme.colors.onSurface,
)

@MultiPreview
@Composable
private fun DefaultDropDownMenuContentPreview() = PreviewBody {
    val options = listOf(
        1 to "Вариант 1",
        2 to "Вариант 2",
        3 to "Вариант 3",
        4 to "Вариант 4",
    )

    DefaultDropDownMenuContent(
        modifier = Modifier.background(AppTheme.componentColors.dropDownMenuColors.containerColor),
        options = options,
        optionName = { it.second },
        onOptionClick = {}
    )
}
