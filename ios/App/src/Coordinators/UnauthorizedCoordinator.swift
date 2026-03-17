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
        
        controller.showAlert = { [weak self] alertModel in
            self?.showErrorAlert(
                title: alertModel.title,
                message: alertModel.message
            )
        }

        navigationController.setViewControllers([controller], animated: false)
    }
    
    private func showErrorAlert(title: String, message: String) {
        let alert = AlertViewController(title: title, message: message)
        navigationController.present(alert, animated: true)
    }
}
