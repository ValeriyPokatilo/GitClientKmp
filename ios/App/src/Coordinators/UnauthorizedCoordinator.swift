import UIKit

final class UnauthorizedCoordinator {

    private let navigationController: UINavigationController

    var onLogin: EmptyBlock?

    init(navigationController: UINavigationController) {
        self.navigationController = navigationController
    }

    func start() {
        let controller = AuthViewController()

        controller.routeToMain = { [weak self] in
            self?.onLogin?()
        }

        navigationController.setViewControllers([controller], animated: false)
    }
}
