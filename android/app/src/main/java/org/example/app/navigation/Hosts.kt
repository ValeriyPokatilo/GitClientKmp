package org.example.app.navigation

sealed interface Hosts {
    val route: String

    data object Auth : Hosts {
        override val route: String = "AuthHost"
    }

    data object Main : Hosts {
        override val route: String = "MainHost"
    }
}
