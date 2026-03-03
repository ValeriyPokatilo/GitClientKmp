package org.example.app.navigation

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import org.example.android.utils.navigation.Screen
import org.example.app.feature.auth.AuthScreen
import org.example.app.feature.profile.ProfileScreen

val mainScreens: PersistentList<Screen> = persistentListOf(
    ProfileScreen,
)

val authScreens: PersistentList<Screen> = persistentListOf(
    AuthScreen
)

val allScreens: PersistentList<Screen> = listOf(
    mainScreens,
    authScreens,
).flatten().toPersistentList()
