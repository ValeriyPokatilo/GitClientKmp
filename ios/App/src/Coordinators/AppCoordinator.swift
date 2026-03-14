import MultiPlatformLibrary
import UIKit

final class AppCoordinator {

    private let window: UIWindow

    private let navigationController = UINavigationController()

    private let router: AppRouter = Koin.instance.getAppRouter()

    private var authCoordinator: AuthCoordinator?
    private var repositoriesListCoordinator: RepositoriesListCoordinator?

    init(window: UIWindow) {
        self.window = window
    }

    func start() {
        window.rootViewController = navigationController

        switch router.getDestination() {

        case is AppRouterRouteAuthRoute:
            showAuthController()

        case is AppRouterRouteRepositoriesRoute:
            showRepositoriesController()

        default:
            showAuthController()
        }
    }

    private func showAuthController() {
        authCoordinator = AuthCoordinator(
            navigationController: navigationController
        )
        authCoordinator?.start()
    }

    private func showRepositoriesController() {
        repositoriesListCoordinator = RepositoriesListCoordinator(
            navigationController: navigationController
        )
        repositoriesListCoordinator?.start()
    }
}
