package org.example.library.utils.logout

import kotlinx.coroutines.flow.Flow

interface LogoutHandler {
    val logoutEvents: Flow<Unit>

    suspend fun onLogout()
}
