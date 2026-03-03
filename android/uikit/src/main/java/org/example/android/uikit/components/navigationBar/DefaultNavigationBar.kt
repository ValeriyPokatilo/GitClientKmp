package org.example.android.uikit.components.navigationBar

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import org.example.android.uikit.R
import org.example.android.uikit.preview.PreviewBody
import org.example.android.uikit.theme.AppTheme

@Composable
fun DefaultNavigationBar(
    items: List<NavigationBarItem>,
    selectedItemIndex: Int,
    onItemClick: (NavigationBarItem) -> Unit,
    modifier: Modifier = Modifier,
    navigationBarColors: NavigationBarColors = defaultNavigationBarColors(),
) = BaseNavigationBar(
    modifier = modifier,
    items = items,
    selectedItemIndex = selectedItemIndex,
    onItemClick = onItemClick,
    navigationBarColors = navigationBarColors,
)

@Composable
internal fun defaultNavigationBarColors() = NavigationBarColors(
    backgroundColor = AppTheme.colors.bottomNavBar,
    selectedItemColor = AppTheme.colors.secondaryContainer,
    unselectedItemColor = AppTheme.colors.transparent,
    selectedContentColor = AppTheme.colors.onSurfaceVariant,
    unselectedContentColor = AppTheme.colors.onSurfaceVariant,
    badgeBackgroundColor = AppTheme.colors.error,
    badgeContentColor = AppTheme.colors.onError,
)

@PreviewLightDark
@Composable
private fun DefaultNavigationBarPreview() = PreviewBody {
    DefaultNavigationBar(
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
    )
}
