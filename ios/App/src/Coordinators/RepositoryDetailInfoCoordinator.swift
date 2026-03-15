import UIKit

final class RepositoryDetailInfoCoordinator {

    private let navigationController: UINavigationController
    var onLogout: EmptyBlock?

    init(navigationController: UINavigationController) {
        self.navigationController = navigationController
    }

    func start() {
        let controller = RepositoryDetailInfoViewController()

        controller.logout = { [weak self] in
            self?.onLogout?()
        }

        navigationController.pushViewController(controller, animated: true)
    }
}
