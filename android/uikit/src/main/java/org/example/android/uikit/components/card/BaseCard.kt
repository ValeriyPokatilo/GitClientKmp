package org.example.android.uikit.components.card

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import org.example.android.uikit.components.ExperimentalComponent
import org.example.android.uikit.preview.PreviewBody
import org.example.android.uikit.theme.AppTheme

@ExperimentalComponent("Check in real project")
@Composable
internal fun BaseCard(
    modifier: Modifier = Modifier,
    cardElevation: CardElevation = CardDefaults.cardElevation(),
    shape: Shape = RoundedCornerShape(20.dp),
    enabled: Boolean = true,
    onClick: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        enabled = enabled,
        onClick = onClick,
        shape = shape,
        colors = CardColors(
            containerColor = AppTheme.colors.background,
            contentColor = contentColorFor(AppTheme.colors.background),
            disabledContainerColor = AppTheme.colors.background,
            disabledContentColor = contentColorFor(AppTheme.colors.background),
        ),
        elevation = cardElevation,
        content = content
    )
}

@PreviewLightDark
@Composable
private fun BaseCardPreview() = PreviewBody {
    BaseCard {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        )
    }
}
