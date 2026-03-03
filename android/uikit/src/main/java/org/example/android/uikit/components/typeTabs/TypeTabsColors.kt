package org.example.android.uikit.components.typeTabs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import org.example.android.uikit.theme.AppTheme

@Immutable
data class TypeTabsColors(
    val selectedContainerColor: Color,
    val unselectedContainerColor: Color,
    val disabledContainerColor: Color,
    val selectedContentColor: Color,
    val unselectedContentColor: Color,
    val disabledContentColor: Color,
    val borderColor: Color,
)

/**
 * All available colors for TypeTabs:
 * containerColor - selected, unselected, disabled
 * contentColor - selected, unselected, disabled
 * borderColor - default
 */
@Composable
internal fun defaultTypeTabsColors() = TypeTabsColors(
    selectedContainerColor = AppTheme.colors.secondaryContainer,
    unselectedContainerColor = AppTheme.colors.surface,
    disabledContainerColor = AppTheme.colors.surface,
    selectedContentColor = AppTheme.colors.onSurface,
    unselectedContentColor = AppTheme.colors.onSurface,
    disabledContentColor = AppTheme.colors.onSurface.copy(alpha = 0.38f),
    borderColor = AppTheme.colors.outline,
)
