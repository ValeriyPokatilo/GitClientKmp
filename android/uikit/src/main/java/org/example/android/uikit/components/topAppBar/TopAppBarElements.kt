package org.example.android.uikit.components.topAppBar

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.example.android.uikit.theme.AppTheme

@Composable
fun TopAppBarTitle(
    text: String
) = Text(
    modifier = Modifier.padding(horizontal = 8.dp),
    text = text,
    style = AppTheme.typography.headline.small,
    color = AppTheme.colors.onSurface,
    maxLines = 1,
    overflow = TextOverflow.Ellipsis
)

@Composable
fun TopAppBarIcon(
    painter: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = AppTheme.colors.onSurface,
) {
    IconButton(
        modifier = modifier.size(40.dp),
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painter,
            contentDescription = null,
            tint = tint
        )
    }
}
