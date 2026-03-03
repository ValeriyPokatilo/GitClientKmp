package org.example.app.navigation

import org.example.android.utils.navigation.BottomBarItem
import org.example.app.R
import org.example.app.feature.profile.ProfileScreen

object BottomBarItems {
    // TODO: add correct items
    val profile = BottomBarItem(
        screenName = ProfileScreen.screenName,
        labelResId = R.string.app_name,
        iconResId = org.example.android.uikit.R.drawable.ic_profile,
    )
}
