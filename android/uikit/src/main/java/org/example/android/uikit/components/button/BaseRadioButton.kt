package org.example.android.uikit.components.button

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.example.android.uikit.components.ExperimentalComponent
import org.example.android.uikit.preview.PreviewBody
import org.example.android.uikit.theme.AppTheme

@ExperimentalComponent("Not presented in DS, check in real project")
@Composable
internal fun BaseRadioButton(
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    selectedBgColor: Color = AppTheme.colors.primary,
    unselectedBgColor: Color = AppTheme.colors.secondaryFocus,
    selectedIndicatorColor: Color = AppTheme.colors.onPrimary,
    unselectedIndicatorColor: Color = AppTheme.colors.onPrimary,
) {
    val dotRadiusFraction = animateFloatAsState(
        targetValue = if (isSelected) .33f else .8f,
        animationSpec = tween(durationMillis = 100),
        label = "dotRadiusFraction"
    )
    Canvas(
        modifier
            .wrapContentSize(Alignment.Center)
            .size(size)
    ) {
        drawCircle(color = if (isSelected) selectedBgColor else unselectedBgColor)

        drawCircle(
            color = if (isSelected) selectedIndicatorColor else unselectedIndicatorColor,
            radius = this.size.minDimension * .5f * dotRadiusFraction.value
        )
    }
}

@Composable
@PreviewLightDark
private fun RadioButtonPreview() = PreviewBody {
    Row {
        BaseRadioButton(isSelected = true, size = 20.dp)
        BaseRadioButton(isSelected = false, size = 20.dp)
    }
}
