package org.example.android.uikit.components.typeTabs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import org.example.android.uikit.preview.MultiPreview
import org.example.android.uikit.preview.PreviewColumn
import org.example.android.uikit.theme.AppTheme

@Composable
fun ThreeTypeTabs(
    firstType: TypeTabData,
    secondType: TypeTabData,
    thirdType: TypeTabData,
    modifier: Modifier = Modifier,
    showDividers: Boolean = true,
    colors: TypeTabsColors = AppTheme.componentColors.typeTabsColors,
) {
    Row(
        modifier = modifier
            .height(40.dp)
            .border(
                width = 1.dp,
                color = colors.borderColor,
                shape = AppTheme.shapes.m
            )
    ) {
        TypeTab(
            modifier = Modifier.weight(1f),
            data = firstType,
            colors = colors,
            shape = RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp),
        )

        if (showDividers) {
            Spacer(
                modifier = Modifier
                    .width(1.dp)
                    .fillMaxHeight()
                    .background(colors.borderColor)
            )
        }

        TypeTab(
            modifier = Modifier.weight(1f),
            data = secondType,
            colors = colors,
            shape = RectangleShape,
        )

        if (showDividers) {
            Spacer(
                modifier = Modifier
                    .width(1.dp)
                    .fillMaxHeight()
                    .background(colors.borderColor)
            )
        }

        TypeTab(
            modifier = Modifier.weight(1f),
            data = thirdType,
            colors = colors,
            shape = RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp),
        )
    }
}

@MultiPreview
@Composable
private fun ThreeTypeTabsPreview() = PreviewColumn {
    ThreeTypeTabs(
        modifier = Modifier.fillMaxWidth(),
        firstType = TypeTabData(
            title = "Новые",
            isSelected = true,
            onSelect = {}
        ),
        secondType = TypeTabData(
            title = "Начатые",
            isSelected = false,
            onSelect = {}
        ),
        thirdType = TypeTabData(
            title = "Прошедшие",
            isSelected = false,
            onSelect = {}
        ),
    )
}
