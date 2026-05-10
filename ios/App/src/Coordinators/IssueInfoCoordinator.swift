import UIKit

final class IssueInfoCoordinator {
    private let navigationController: UINavigationController
    private let owner: String
    private let repositoryName: String
    private let issueNumber: Int

    init(
        navigationController: UINavigationController,
        owner: String,
        repositoryName: String,
        issueNumber: Int
    ) {
        self.navigationController = navigationController
        self.owner = owner
        self.repositoryName = repositoryName
        self.issueNumber = issueNumber
    }

    func start() {
        let controller = IssueInfoViewController(
            owner: owner,
            repositoryName: repositoryName,
            issueNumber: issueNumber
        )

        navigationController.pushViewController(controller, animated: true)
    }
}
