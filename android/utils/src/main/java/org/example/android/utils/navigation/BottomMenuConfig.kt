package org.example.android.utils.navigation

sealed interface BottomMenuConfig {
    data object Hidden : BottomMenuConfig
    data class Visible(val bottomItem: BottomBarItem) : BottomMenuConfig
}
