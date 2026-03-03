package org.example.android.uikit.components.item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import org.example.android.uikit.R
import org.example.android.uikit.components.ExperimentalComponent
import org.example.android.uikit.preview.PreviewBody
import org.example.android.uikit.theme.AppTheme

@Suppress("LongMethod")
@ExperimentalComponent("Need check on project")
@Composable
internal fun BaseItem(
    modifier: Modifier = Modifier,
    backgroundColor: Color = AppTheme.colors.surface,
    backgroundPadding: PaddingValues = PaddingValues(
        vertical = 8.dp,
        horizontal = 16.dp
    ),
    leading: @Composable (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null,
    content: @Composable (() -> Unit)? = null,
) {
    Layout(
        modifier = Modifier
            .background(backgroundColor)
            .padding(backgroundPadding)
            .then(modifier),
        content = {
            Box(
                modifier = Modifier.layoutId("leading")
            ) {
                leading?.invoke()
            }
            Box(
                modifier = Modifier
                    .layoutId("content")
                    .wrapContentSize()
            ) {
                content?.invoke()
            }
            Box(
                modifier = Modifier.layoutId("trailing")
            ) {
                trailing?.invoke()
            }
        }
    ) { measurables, constraints ->
        val leadingPlaceable =
            measurables.first { it.layoutId == "leading" }
                .measure(constraints)

        val trailingPlaceable =
            measurables.first { it.layoutId == "trailing" }
                .measure(constraints)

        val contentWidth = constraints.maxWidth -
            leadingPlaceable.width - 16.dp.roundToPx() -
            trailingPlaceable.width - 16.dp.roundToPx()

        val contentConstraints: Constraints = constraints.copy(
            minWidth = constraints.minWidth,
            maxWidth = contentWidth,
            minHeight = constraints.minHeight,
            maxHeight = constraints.maxHeight
        )

        val contentPlaceable = measurables
            .first { it.layoutId == "content" }
            .measure(contentConstraints)

        val layoutHeight = contentPlaceable.height.coerceAtLeast(0)

        layout(constraints.maxWidth, layoutHeight) {
            leadingPlaceable.placeRelative(
                x = 0,
                y = (layoutHeight - leadingPlaceable.height) / 2
            )
            contentPlaceable.placeRelative(
                x = (leadingPlaceable.width + 16.dp.roundToPx()),
                y = (layoutHeight - contentPlaceable.height) / 2
            )
            trailingPlaceable.placeRelative(
                x = constraints.maxWidth - trailingPlaceable.width,
                y = (layoutHeight - trailingPlaceable.height) / 2
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun BaseItemPreview() = PreviewBody {
    BaseItem(
        leading = {
            Icon(
                modifier = Modifier
                    .size(24.dp),
                painter = painterResource(id = R.drawable.ic_label),
                tint = AppTheme.colors.onSurface,
                contentDescription = null,
            )
        },
        trailing = {
            Checkbox(
                modifier = Modifier
                    .padding(0.dp),
                checked = false,
                onCheckedChange = null,
            )
        }
    ) {
        Text(
            text = "Получать уведомления",
            color = AppTheme.colors.onSurface,
            style = AppTheme.typography.body.large
        )
    }
}
