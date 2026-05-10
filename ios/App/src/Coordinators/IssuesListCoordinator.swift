import MultiPlatformLibrary
import UIKit

final class IssuesListCoordinator {
    private let navigationController: UINavigationController
    private let owner: String
    private let repositoryName: String

    private var issuesListController: IssuesListViewController?

    init(
        navigationController: UINavigationController,
        owner: String,
        repositoryName: String
    ) {
        self.navigationController = navigationController
        self.owner = owner
        self.repositoryName = repositoryName
    }

    func start() {
        let controller = IssuesListViewController(
            owner: owner,
            repositoryName: repositoryName
        )

        issuesListController = controller

        navigationController.pushViewController(controller, animated: true)
    }

    private func refreshIssuesList() {
        issuesListController?.refresh()
    }
}
