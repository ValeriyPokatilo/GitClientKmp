package org.example.android.uikit.components.checkBox

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.android.uikit.preview.MultiPreview
import org.example.android.uikit.preview.PreviewBody
import org.example.android.uikit.theme.AppTheme

@Composable
fun DefaultCheckbox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: CheckboxColors = AppTheme.componentColors.checkboxColors,
    interactionSource: MutableInteractionSource? = null,
) {
    BaseCheckbox(
        modifier = modifier,
        checked = checked,
        onCheckedChange = onCheckedChange,
        enabled = enabled,
        colors = colors,
        interactionSource = interactionSource,
    )
}

@Composable
internal fun defaultCheckboxColors() = CheckboxDefaults.colors().copy(
    checkedCheckmarkColor = AppTheme.colors.onPrimary,
    checkedBoxColor = AppTheme.colors.primary,
    checkedBorderColor = AppTheme.colors.primary
)

@MultiPreview
@Composable
private fun DefaultCheckboxPreview() = PreviewBody {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DefaultCheckbox(
            checked = false,
            onCheckedChange = {},
            enabled = true,
        )
        DefaultCheckbox(
            checked = true,
            onCheckedChange = {},
            enabled = true,
        )
        DefaultCheckbox(
            checked = false,
            onCheckedChange = {},
            enabled = false,
        )
        DefaultCheckbox(
            checked = true,
            onCheckedChange = {},
            enabled = false,
        )
    }
}
