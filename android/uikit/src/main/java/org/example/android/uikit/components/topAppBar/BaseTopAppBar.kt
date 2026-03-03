@file:Suppress("TopLevelPropertyNaming")

package org.example.android.uikit.components.topAppBar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.example.android.uikit.components.utils.measure
import org.example.android.uikit.components.utils.measureOrNull
import org.example.android.uikit.preview.PreviewBody
import org.example.android.uikit.preview.providers.BooleanProvider
import org.example.android.uikit.theme.AppTheme
import kotlin.math.roundToInt

@Suppress("LongMethod")
@Composable
internal fun BaseTopAppBar(
    modifier: Modifier = Modifier,
    height: Dp = 52.dp,
    horizontalPadding: Dp = 8.dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 8.dp),
    containerColor: Color = AppTheme.colors.background,
    windowInsets: WindowInsets = WindowInsets.systemBars
        .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top),
    isContentCentered: Boolean = false,
    leading: @Composable (BoxScope.() -> Unit)? = null,
    trailing: @Composable (BoxScope.() -> Unit)? = null,
    content: @Composable (BoxScope.() -> Unit)? = null,
) {
    val layoutDirection = LocalLayoutDirection.current
    val heightPx = LocalDensity.current.run { height.toPx() }
    val layoutHeight = remember(!heightPx.isNaN()) {
        if (!heightPx.isNaN()) {
            heightPx.roundToInt().coerceAtLeast(0)
        } else {
            0
        }
    }

    Layout(
        modifier = Modifier
            .background(containerColor)
            .padding(horizontal = horizontalPadding)
            .windowInsetsPadding(windowInsets)
            .then(modifier),
        content = {
            leading?.let {
                Box(
                    modifier = Modifier
                        .layoutId(LeadingLayoutId)
                        .padding(end = contentPadding.calculateStartPadding(layoutDirection))
                ) {
                    leading(this)
                }
            }

            Box(
                modifier = Modifier
                    .layoutId(ContentLayoutId)
                    .wrapContentSize()
            ) {
                content?.invoke(this)
            }

            trailing?.let {
                Box(
                    modifier = Modifier
                        .layoutId(TrailingLayoutId)
                        .padding(start = contentPadding.calculateEndPadding(layoutDirection))
                ) {
                    trailing(this)
                }
            }
        }
    ) { measurables, constraints ->
        val leadingPlaceable = measurables.measureOrNull(LeadingLayoutId, constraints)
        val trailingPlaceable = measurables.measureOrNull(TrailingLayoutId, constraints)

        val leadingPadding = leadingPlaceable?.width ?: 0
        val trailingPadding = trailingPlaceable?.width ?: 0

        val contentPlaceable = measurables.measure(
            ContentLayoutId,
            constraints.copy(
                minWidth = 0,
                maxWidth = constraints.maxWidth - leadingPadding - trailingPadding
            )
        )
        val contentX = if (isContentCentered) {
            (constraints.maxWidth - contentPlaceable.width) / 2
        } else {
            leadingPadding
        }

        layout(constraints.maxWidth, layoutHeight) {
            leadingPlaceable?.placeRelative(
                x = 0,
                y = (layoutHeight - leadingPlaceable.height) / 2
            )
            contentPlaceable.placeRelative(
                x = contentX,
                y = (layoutHeight - contentPlaceable.height) / 2
            )
            trailingPlaceable?.placeRelative(
                x = constraints.maxWidth - trailingPlaceable.width,
                y = (layoutHeight - trailingPlaceable.height) / 2
            )
        }
    }
}

private const val LeadingLayoutId = "leading"
private const val TrailingLayoutId = "trailing"
private const val ContentLayoutId = "content"

@PreviewLightDark
@Composable
private fun BaseTopAppBarPreview(
    @PreviewParameter(BooleanProvider::class) isContentCentered: Boolean,
) = PreviewBody {
    BaseTopAppBar(
        isContentCentered = isContentCentered
    ) {
        TopAppBarTitle(text = "Title")
    }
}
