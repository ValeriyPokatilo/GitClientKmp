package org.example.library

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import org.example.library.utils.logout.LogoutHandler

internal class ChannelLogoutHandler : LogoutHandler {
    private val logoutEventsChannel = Channel<Unit>()

    override val logoutEvents: Flow<Unit>
        get() = logoutEventsChannel.receiveAsFlow()

    override suspend fun onLogout() {
        logoutEventsChannel.send(Unit)
    }
}
