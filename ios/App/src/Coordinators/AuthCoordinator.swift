import UIKit

final class AuthCoordinator {

    private let navigationController: UINavigationController
    private var repositoriesListCoordinator: RepositoriesListCoordinator?

    init(navigationController: UINavigationController) {
        self.navigationController = navigationController
    }

    func start() {
        let controller = AuthViewController()

        controller.routeToMain = { [weak self] in
            self?.routeToMain()
        }

        controller.showAlert = { [weak self] alertContent in
            self?.showAlert(title: alertContent.0, message: alertContent.1)
        }

        navigationController.pushViewController(controller, animated: true)
    }

    private func routeToMain() {
        repositoriesListCoordinator = RepositoriesListCoordinator(
            navigationController: navigationController
        )
        repositoriesListCoordinator?.start()
    }

    private func showAlert(title: String, message: String) {
        let alertController = AlertViewController(
            title: title,
            message: message
        )

        navigationController.present(alertController, animated: false)
    }
}
