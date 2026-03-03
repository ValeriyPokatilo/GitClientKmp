package org.example.android.utils.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination

/**
 * Заменить весь стек навигации на другой экран, вне
 * зависимости от содержимого текущего бекстека.
 *
 * То есть после такого перехода если юзер нажмет назад - приложение закроется.
 * История обнуляется данным переходом.
 */
fun NavController.replace(route: String) {
    // удаляем всё что есть из бекстека
    while (this.popBackStack()) {
        Unit
    }
    // делаем переход
    navigate(route) {
        // при чем нам надо текущий экран тоже убрать из стека
        val id: String? = currentBackStackEntry?.id
        if (id != null) {
            popUpTo(id) { inclusive = true }
        }
    }
}

fun NavController.navigateSingleTop(
    screenName: String,
    saveState: Boolean = true,
) {
    navigate(screenName) {
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(graph.findStartDestination().id) {
            this.saveState = saveState
        }
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
    }
}
