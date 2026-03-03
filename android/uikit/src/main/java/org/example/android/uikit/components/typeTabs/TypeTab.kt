package org.example.android.uikit.components.typeTabs

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import org.example.android.uikit.theme.AppTheme

@Composable
fun TypeTab(
    data: TypeTabData,
    shape: Shape,
    modifier: Modifier = Modifier,
    colors: TypeTabsColors = AppTheme.componentColors.typeTabsColors,
) {
    val animatedContainerColor: Color by animateColorAsState(
        targetValue = if (data.isSelected) {
            colors.selectedContainerColor
        } else {
            colors.unselectedContainerColor
        },
        animationSpec = tween(ANIMATION_DURATION_150)
    )
    val animatedContentColor: Color by animateColorAsState(
        targetValue = if (data.isSelected) {
            colors.selectedContentColor
        } else {
            colors.unselectedContentColor
        },
        animationSpec = tween(ANIMATION_DURATION_150)
    )

    Box(
        modifier = modifier
            .fillMaxHeight()
            .clip(shape)
            .background(
                color = animatedContainerColor,
                shape = shape
            )
            .clickable(
                onClick = data.onSelect
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = data.title,
            color = animatedContentColor,
            style = AppTheme.typography.label.large
        )
    }
}

data class TypeTabData(
    val title: String,
    val isSelected: Boolean,
    val onSelect: () -> Unit,
    val isEnabled: Boolean = true,
)

private const val ANIMATION_DURATION_150 = 150
