import UIKit

final class RepositoryDetailInfoCoordinator {

    private let navigationController: UINavigationController
    private let owner: String
    private let repositoryName: String
    private let branch: String

    var onLogout: EmptyBlock?

    init(
        navigationController: UINavigationController,
        owner: String,
        repositoryName: String,
        branch: String
    ) {
        self.navigationController = navigationController
        self.owner = owner
        self.repositoryName = repositoryName
        self.branch = branch
    }

    func start() {
        let controller = RepositoryDetailInfoViewController(
            owner: owner,
            repositoryName: repositoryName,
            branch: branch
        )

        controller.onLogout = { [weak self] in
            self?.onLogout?()
        }

        navigationController.pushViewController(controller, animated: true)
    }
}
