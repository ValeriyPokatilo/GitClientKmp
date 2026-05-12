import MultiPlatformLibrary
import UIKit

final class IssuesListCoordinator {
    private let navigationController: UINavigationController
    private let owner: String
    private let repositoryName: String
    private var createIssueCoordinator: CreateIssueCoordinator?
    private var issueInfoCoordinator: IssueInfoCoordinator?

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

        controller.showCreateIssue = { [weak self] in
            self?.showCreateIssue()
        }

        controller.showDetails = { [weak self] issueNumber in
            self?.showIssueDetails(issueNumber: issueNumber)
        }

        issuesListController = controller

        navigationController.pushViewController(controller, animated: true)
    }

    private func showCreateIssue() {
        let coordinator = CreateIssueCoordinator(
            navigationController: navigationController,
            owner: owner,
            repositoryName: repositoryName
        )

        createIssueCoordinator = coordinator

        coordinator.start()
    }

    private func showIssueDetails(issueNumber: Int) {
        let coordinator = IssueInfoCoordinator(
            navigationController: navigationController,
            owner: owner,
            repositoryName: repositoryName,
            issueNumber: issueNumber
        )

        issueInfoCoordinator = coordinator

        coordinator.start()
    }
}
