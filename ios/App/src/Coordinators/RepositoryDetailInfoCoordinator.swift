import UIKit

final class RepositoryDetailInfoCoordinator {
    
    private let navigationController: UINavigationController
    
    init(navigationController: UINavigationController) {
        self.navigationController = navigationController
    }
    
    func start() {
        let controller = RepositoryDetailInfoViewController()
        navigationController.pushViewController(controller, animated: true)
    }
}
