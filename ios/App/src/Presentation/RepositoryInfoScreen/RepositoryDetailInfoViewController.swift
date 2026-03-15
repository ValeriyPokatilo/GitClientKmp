import UIKit
import MultiPlatformLibrary

final class RepositoryDetailInfoViewController: UIViewController {
    
    var logout: EmptyBlock?

    override func viewDidLoad() {
        super.viewDidLoad()
        setupNavigation()
    }
    
    private func setupNavigation() {
        navigationItem.title = "Repo Name"

        let button = UIBarButtonItem(
            image: R.image.ic_logout(),
            style: .plain,
            target: self,
            action: #selector(onLogoutTap)
        )

        button.tintColor = .white

        navigationItem.rightBarButtonItem = button
        
    }
    
    @objc private func onLogoutTap() {
        // TODO: - viewModel.onLogoutButtonPressed()
        logout?()
    }
}
