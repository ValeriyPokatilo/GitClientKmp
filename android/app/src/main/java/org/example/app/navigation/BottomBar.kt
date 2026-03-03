package org.example.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import org.example.android.uikit.components.navigationBar.DefaultNavigationBar
import org.example.android.uikit.components.navigationBar.NavigationBarItem
import org.example.android.utils.navigation.BottomBarItem
import org.example.android.utils.navigation.navigateSingleTop

@Composable
fun BottomBar(
    navController: NavHostController,
    selectedItem: BottomBarItem,
) {
    val bottomBarItems = remember {
        listOf(
            BottomBarItems.profile,
            // TODO: add other BottomBarItems
        )
    }
    val navigationItems = bottomBarItems.mapIndexed { index, item ->
        NavigationBarItem(
            id = index,
            icon = painterResource(item.iconResId),
            text = stringResource(item.labelResId),
        )
    }

    DefaultNavigationBar(
        items = navigationItems,
        selectedItemIndex = bottomBarItems.indexOfFirst { it == selectedItem },
        onItemClick = { item ->
            val bottomBarItem = bottomBarItems[item.id]
            /* a condition for prohibiting multitap */
            if (bottomBarItem.screenName != navController.currentBackStackEntry?.destination?.route) {
                navController.navigateSingleTop(bottomBarItem.screenName, saveState = false)
            }
        },
    )
}
