@file:Suppress("MagicNumber")

package org.example.android.uikit.components.navigationBar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.example.android.uikit.R
import org.example.android.uikit.preview.MultiPreview
import org.example.android.uikit.preview.PreviewBody

@Composable
private fun NavigationBarVariants(text: String?) {
    val items = listOf(
        NavigationBarItem(
            id = 1,
            icon = painterResource(id = R.drawable.ic_bag),
            text = text,
        ),
        NavigationBarItem(
            id = 2,
            icon = painterResource(id = R.drawable.ic_bag),
            text = text,
            badge = "5",
        ),
        NavigationBarItem(
            id = 3,
            icon = painterResource(id = R.drawable.ic_bag),
            text = text,
        ),
        NavigationBarItem(
            id = 4,
            icon = painterResource(id = R.drawable.ic_bag),
            text = text,
            badge = "1234",
        ),
        NavigationBarItem(
            id = 5,
            icon = painterResource(id = R.drawable.ic_bag),
            text = text,
        ),
    )

    DefaultNavigationBar(
        modifier = Modifier,
        items = items,
        selectedItemIndex = 0,
        onItemClick = {},
    )
    Spacer(Modifier.height(23.dp))
    DefaultNavigationBar(
        modifier = Modifier,
        items = items.take(4),
        selectedItemIndex = 0,
        onItemClick = {},
    )
    Spacer(Modifier.height(23.dp))
    DefaultNavigationBar(
        modifier = Modifier,
        items = items.take(3),
        selectedItemIndex = 0,
        onItemClick = {},
    )
    Spacer(Modifier.height(23.dp))
    DefaultNavigationBar(
        modifier = Modifier,
        items = items.take(2),
        selectedItemIndex = 0,
        onItemClick = {},
    )
}

@MultiPreview
@Composable
private fun BottomTabBarPreview() = PreviewBody {
    Column(
        modifier = Modifier.padding(24.dp)
    ) {
        NavigationBarVariants(text = "Label")
        Spacer(Modifier.height(23.dp))
        NavigationBarVariants(text = null)
    }
}
