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

        controller.logout = { [weak self] in
            self?.onLogout?()
        }

        controller.showDetails = { [weak self] details in
            self?.showRepositoryDetails(
                owner: details.owner,
                repositoryName: details.repositoryName,
                branch: details.branch
            )
        }

        navigationController.setViewControllers([controller], animated: false)
    }

    func showRepositoryDetails(
        owner: String,
        repositoryName: String,
        branch: String
    ) {
        let coordinator = RepositoryDetailInfoCoordinator(
            navigationController: navigationController,
            owner: owner,
            repositoryName: repositoryName,
            branch: branch
        )

        coordinator.onLogout = { [weak self] in
            self?.onLogout?()
        }

        repositoryDetailCoordinator = coordinator
        coordinator.start()
    }
}
