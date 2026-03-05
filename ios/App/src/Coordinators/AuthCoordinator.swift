import UIKit

final class AuthCoordinator {
    
    private let navigationController: UINavigationController
    
    init(navigationController: UINavigationController) {
        self.navigationController = navigationController
    }
    
    func start() {
        let controller = AuthViewController()
        navigationController.pushViewController(controller, animated: true)
    }
}
