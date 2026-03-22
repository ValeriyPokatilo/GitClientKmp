import Combine
import MultiPlatformLibrary
import SwiftUI

struct LogoutNavigationHookView<Content: View>: View {
    private var logoutHandler: LogoutHandler = Koin.instance.getLogoutHandler()

    let onLogout: () -> Void
    let content: () -> Content

    init(onLogout: @escaping () -> Void, @ViewBuilder content: @escaping () -> Content) {
        self.onLogout = onLogout
        self.content = content
    }

    var body: some View {
        content()
            .onReceive(
                logoutHandler.logoutEvents.toPublisher()
                    .catch { _ in Empty<KotlinUnit, Never>() }
                    .assertNoFailure()
            ) { _ in
                onLogout()
            }
    }
}
