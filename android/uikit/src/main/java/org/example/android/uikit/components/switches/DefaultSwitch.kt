package org.example.android.uikit.components.switches

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.Icon
import androidx.compose.material3.SwitchColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import org.example.android.uikit.R
import org.example.android.uikit.components.ExperimentalComponent
import org.example.android.uikit.preview.MultiPreview
import org.example.android.uikit.preview.PreviewRow
import org.example.android.uikit.theme.AppTheme

@Composable
fun DefaultSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    icon: Painter? = null,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    colors: SwitchColors = AppTheme.componentColors.switchColors,
) {
    BaseSwitch(
        modifier = modifier,
        checked = checked,
        onCheckedChange = onCheckedChange,
        enabled = enabled,
        colors = colors,
        interactionSource = interactionSource,
        thumbContent = icon?.let {
            {
                Icon(
                    painter = icon,
                    contentDescription = null,
                )
            }
        }
    )
}

/**
 * All available colors for Switch:
 * thumbColor - checked, unchecked; disabled - checked, unchecked
 * trackColor - checked, unchecked; disabled - checked, unchecked
 * borderColor - checked, unchecked; disabled - checked, unchecked
 * iconColor - checked, unchecked; disabled - checked, unchecked
 */
@ExperimentalComponent("Для состояния disabled в дизайне указаны кастомные цвета, ждем цвета из ДС")
@Composable
fun defaultSwitchColors(): SwitchColors = SwitchColors(
    checkedThumbColor = AppTheme.colors.onPrimary,
    checkedTrackColor = AppTheme.colors.primary,
    checkedBorderColor = AppTheme.colors.transparent,
    checkedIconColor = AppTheme.colors.onPrimaryContainer,
    uncheckedThumbColor = AppTheme.colors.outline,
    uncheckedTrackColor = AppTheme.colors.surfaceVariant,
    uncheckedBorderColor = AppTheme.colors.outline,
    uncheckedIconColor = AppTheme.colors.onPrimary,
    disabledCheckedThumbColor = AppTheme.colors.surface,
    disabledCheckedIconColor = AppTheme.colors.onSurface,
    disabledCheckedTrackColor = AppTheme.colors.surface,
    disabledCheckedBorderColor = AppTheme.colors.transparent,
    disabledUncheckedIconColor = AppTheme.colors.surfaceVariant,
    disabledUncheckedThumbColor = AppTheme.colors.surface,
    disabledUncheckedTrackColor = AppTheme.colors.transparent,
    disabledUncheckedBorderColor = AppTheme.colors.surface,
)

@MultiPreview
@Composable
private fun SwitchPreview() = PreviewRow {
    DefaultSwitch(
        checked = true,
        onCheckedChange = {},
        icon = painterResource(R.drawable.ic_check),
    )

    DefaultSwitch(
        checked = false,
        onCheckedChange = {},
        icon = painterResource(R.drawable.ic_check),
    )

    DefaultSwitch(
        checked = true,
        enabled = false,
        onCheckedChange = {},
        icon = painterResource(R.drawable.ic_check),
    )

    DefaultSwitch(
        checked = false,
        enabled = false,
        onCheckedChange = {},
        icon = painterResource(R.drawable.ic_check),
    )
}
