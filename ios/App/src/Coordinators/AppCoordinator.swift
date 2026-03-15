import MultiPlatformLibrary
import UIKit

final class AppCoordinator {

    private let window: UIWindow

    private let navigationController = UINavigationController()

    private let router: AppRouter = Koin.instance.getAppRouter()

    private var authorizedCoordinator: AuthorizedCoordinator?
    private var unauthorizedCoordinator: UnauthorizedCoordinator?

    init(window: UIWindow) {
        self.window = window
    }

    func start() {
        window.rootViewController = navigationController
        window.makeKeyAndVisible()

        switch router.getDestination() {
        case is AppRouterRouteRepositoriesRoute:
            authorizedFlow()
        default:
            unauthorizedFlow()
        }
    }

    func authorizedFlow() {
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

    func unauthorizedFlow() {
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

    func logout() {
        unauthorizedFlow()
    }
}
