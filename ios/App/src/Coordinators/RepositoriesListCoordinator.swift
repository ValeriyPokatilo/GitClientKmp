import UIKit

final class RepositoriesListCoordinator {
    
    private let navigationController: UINavigationController
    
    init(navigationController: UINavigationController) {
        self.navigationController = navigationController
    }
    
    func start() {
        let controller = RepositoriesListViewController()
        navigationController.pushViewController(controller, animated: true)
    }
}
