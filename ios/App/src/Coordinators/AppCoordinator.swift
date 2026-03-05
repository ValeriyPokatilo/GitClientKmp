import UIKit

final class AppCoordinator {
    
    private let window: UIWindow
    
    private let navigationController = UINavigationController()

    private var authCoordinator: AuthCoordinator?
    private var repositoriesListCoordinator: RepositoriesListCoordinator?
    
    init(window: UIWindow) {
        self.window = window
    }
    
    func start() {
        window.rootViewController = navigationController
        showAuthController()
    }
    
    private func showAuthController() {
        authCoordinator = AuthCoordinator(navigationController: navigationController)
        authCoordinator?.start()
    }
    
    private func showRepositoriesController() {
        repositoriesListCoordinator = RepositoriesListCoordinator(navigationController: navigationController)
        repositoriesListCoordinator?.start()
    }
}
