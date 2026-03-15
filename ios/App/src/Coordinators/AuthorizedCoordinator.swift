import UIKit

final class AuthorizedCoordinator {

    private let navigationController: UINavigationController

    private var repositoryDetailCoordinator: RepositoryDetailInfoCoordinator?

    var onLogout: EmptyBlock?

    init(navigationController: UINavigationController) {
        self.navigationController = navigationController
    }

    func start() {
        let controller = RepositoriesListViewController()

        controller.onLogout = { [weak self] in
            self?.onLogout?()
        }

        controller.showDetails = { [weak self] in
            self?.showRepositoryDetails()
        }

        navigationController.setViewControllers([controller], animated: false)
    }

    func showRepositoryDetails() {
        let coordinator = RepositoryDetailInfoCoordinator(
            navigationController: navigationController
        )
        
        coordinator.onLogout = { [weak self] in
            self?.onLogout?()
        }

        repositoryDetailCoordinator = coordinator
        coordinator.start()
    }
}
