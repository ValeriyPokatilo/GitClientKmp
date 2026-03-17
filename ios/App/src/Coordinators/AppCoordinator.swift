import MultiPlatformLibrary
import UIKit

final class AppCoordinator {

    private let window: UIWindow
    private let navigationController: UINavigationController
    private let router: AppRouter

    private var authorizedCoordinator: AuthorizedCoordinator?
    private var unauthorizedCoordinator: UnauthorizedCoordinator?

    init(
        window: UIWindow,
        router: AppRouter = Koin.instance.getAppRouter(),
        navigationController: UINavigationController = UINavigationController()
    ) {
        self.window = window
        self.router = router
        self.navigationController = navigationController
    }

    func start() {
        window.rootViewController = navigationController

        switch router.getDestination() {
        case is AppRouterRouteRepositoriesRoute:
            authorizedFlow()
        default:
            unauthorizedFlow()
        }
    }

    private func authorizedFlow() {
        let coordinator = AuthorizedCoordinator(
            navigationController: navigationController
        )

        coordinator.onLogout = { [weak self] in
            self?.logout()
        }

        coordinator.start()

        authorizedCoordinator = coordinator
        unauthorizedCoordinator = nil
    }

    private func unauthorizedFlow() {
        let coordinator = UnauthorizedCoordinator(
            navigationController: navigationController
        )

        coordinator.onLogin = { [weak self] in
            self?.authorizedFlow()
        }

        coordinator.start()

        unauthorizedCoordinator = coordinator
        authorizedCoordinator = nil
    }

    private func logout() {
        unauthorizedFlow()
    }
}
