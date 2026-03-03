package org.example.android.uikit.components.navigationBar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.example.android.uikit.R
import org.example.android.uikit.preview.MultiPreview
import org.example.android.uikit.preview.PreviewBody
import org.example.android.uikit.theme.AppTheme

@Suppress("LongMethod")
@Composable
internal fun NavigationBarButton(
    icon: Painter,
    selected: Boolean,
    onClick: () -> Unit,
    colors: NavigationBarColors,
    modifier: Modifier = Modifier,
    text: String? = null,
    badge: String? = null,
) {
    val backgroundColor = colors.itemColor(selected)
    val contentColor = colors.contentColor(selected)

    Layout(
        modifier = modifier
            .fillMaxHeight()
            .padding(4.dp)
            .background(
                color = backgroundColor,
                shape = NAVIGATION_ITEM_SHAPE
            )
            .clip(NAVIGATION_ITEM_SHAPE)
            .clickable(
                onClick = onClick,
                role = Role.Button,
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple()
            ),
        content = {
            Icon(
                modifier = Modifier
                    .layoutId("icon")
                    .size(24.dp),
                painter = icon,
                contentDescription = null,
                tint = contentColor,
            )
            text?.let {
                Text(
                    modifier = Modifier
                        .layoutId("label")
                        .padding(horizontal = 2.dp),
                    text = text,
                    maxLines = 1,
                    color = contentColor,
                    overflow = TextOverflow.Ellipsis,
                    style = AppTheme.typography.label.medium,
                )
            }
            badge?.let {
                Text(
                    modifier = Modifier
                        .layoutId("badge")
                        .sizeIn(minWidth = 16.dp, minHeight = 16.dp)
                        .background(
                            color = colors.badgeBackgroundColor,
                            shape = RoundedCornerShape(100.dp)
                        )
                        .padding(horizontal = 4.dp),
                    text = badge,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    color = colors.badgeContentColor,
                    overflow = TextOverflow.Ellipsis,
                    style = AppTheme.typography.label.medium,
                )
            }
        },
        measurePolicy = { measurables, constraints ->
            val iconPlaceable =
                measurables.first { it.layoutId == "icon" }.measure(
                    constraints.copy(
                        minWidth = 24.dp.roundToPx(),
                        maxWidth = constraints.maxWidth,
                        minHeight = 24.dp.roundToPx(),
                        maxHeight = constraints.maxHeight,
                    )
                )
            val labelPlaceable =
                measurables.firstOrNull { it.layoutId == "label" }?.measure(
                    constraints.copy(
                        minWidth = 0,
                        maxWidth = constraints.maxWidth,
                        minHeight = 0,
                        maxHeight = constraints.maxHeight,
                    )
                )
            val badgePlaceable =
                measurables.firstOrNull { it.layoutId == "badge" }?.measure(
                    constraints.copy(
                        minWidth = 0,
                        maxWidth = constraints.maxWidth,
                        minHeight = 0,
                        maxHeight = constraints.maxHeight,
                    )
                )
            val layoutWidth = constraints.maxWidth
            val layoutHeight = constraints.minHeight
            val iconY = if (labelPlaceable == null) {
                (layoutHeight - iconPlaceable.height) / 2
            } else {
                (layoutHeight - iconPlaceable.height - labelPlaceable.height - 2.dp.roundToPx()) / 2
            }
            val badgeX = if (badgePlaceable == null) {
                0
            } else {
                val x = if (badgePlaceable.width > 34.dp.roundToPx()) {
                    layoutWidth - badgePlaceable.width
                } else {
                    layoutWidth - badgePlaceable.width + iconPlaceable.width - 6.dp.roundToPx()
                }
                x / 2
            }
            layout(layoutWidth, layoutHeight) {
                iconPlaceable.placeRelative(
                    x = (layoutWidth - iconPlaceable.width) / 2,
                    y = iconY
                )
                labelPlaceable?.placeRelative(
                    x = (layoutWidth - labelPlaceable.width) / 2,
                    y = (layoutHeight - labelPlaceable.height + iconPlaceable.height + 2.dp.roundToPx()) / 2
                )
                badgePlaceable?.placeRelative(
                    x = badgeX,
                    y = (layoutHeight - badgePlaceable.height - iconPlaceable.height - 4.dp.roundToPx()) / 2
                )
            }
        },
    )
}

private val NAVIGATION_ITEM_SHAPE = RoundedCornerShape(4.dp)

@MultiPreview
@Composable
private fun NavigationBarButtonPreview() = PreviewBody {
    NavigationBarButton(
        modifier = Modifier.size(64.dp),
        icon = painterResource(id = R.drawable.ic_bag),
        text = "Label",
        badge = "12",
        selected = true,
        onClick = {},
        colors = AppTheme.componentColors.navigationBarColors
    )
}
