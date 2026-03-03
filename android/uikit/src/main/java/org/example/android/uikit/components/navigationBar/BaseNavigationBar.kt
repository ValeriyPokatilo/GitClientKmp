package org.example.android.uikit.components.navigationBar

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.example.android.uikit.R
import org.example.android.uikit.preview.MultiPreview
import org.example.android.uikit.preview.PreviewBody

private val NAVIGATION_BAR_HEIGHT = 56.dp

data class NavigationBarItem(
    val id: Int,
    val icon: Painter,
    val text: String? = null,
    val badge: String? = null
)

@Composable
internal fun BaseNavigationBar(
    items: List<NavigationBarItem>,
    selectedItemIndex: Int,
    onItemClick: (NavigationBarItem) -> Unit,
    navigationBarColors: NavigationBarColors,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars),
        color = navigationBarColors.backgroundColor,
    ) {
        SubcomposeLayout(
            modifier = Modifier
                .fillMaxWidth()
                .height(NAVIGATION_BAR_HEIGHT)
                .padding(
                    vertical = 0.dp,
                    horizontal = 4.dp
                )
        ) { constraints ->
            val wholeRowWidth = constraints.maxWidth

            val measurables = subcompose("Tabs") {
                items.forEachIndexed { index, tab ->
                    NavigationBarButton(
                        icon = tab.icon,
                        text = tab.text,
                        badge = tab.badge,
                        selected = index == selectedItemIndex,
                        colors = navigationBarColors,
                        onClick = {
                            onItemClick(tab)
                        },
                    )
                }
            }
            val tabCount = measurables.size
            val tabWidth = wholeRowWidth / tabCount
            val tabPlaceables = measurables.map {
                it.measure(
                    constraints = constraints.copy(minWidth = tabWidth, maxWidth = tabWidth)
                )
            }
            val tabRowHeight = tabPlaceables.minByOrNull { it.height }?.height ?: 0

            layout(wholeRowWidth, tabRowHeight) {
                tabPlaceables.forEachIndexed { index, placeable ->
                    placeable.placeRelative(
                        x = index * tabWidth,
                        y = 0
                    )
                }
            }
        }
    }
}

@MultiPreview
@Composable
private fun BaseNavigationBarPreview() = PreviewBody {
    BaseNavigationBar(
        modifier = Modifier,
        items = listOf(
            NavigationBarItem(
                id = 1,
                icon = painterResource(id = R.drawable.ic_bag),
                text = "Label",
            ),
            NavigationBarItem(
                id = 2,
                icon = painterResource(id = R.drawable.ic_bag),
                text = "Label",
                badge = "1",
            ),
            NavigationBarItem(
                id = 3,
                icon = painterResource(id = R.drawable.ic_bag),
                text = "Longlabelfortestgagalongtexts",
            ),
            NavigationBarItem(
                id = 4,
                icon = painterResource(id = R.drawable.ic_bag),
                text = "Label",
            ),
        ),
        selectedItemIndex = 0,
        onItemClick = {},
        navigationBarColors = defaultNavigationBarColors()
    )
}
