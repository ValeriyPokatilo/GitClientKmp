import MultiPlatformLibrary
import UIKit

final class RepositoriesListViewController: UITableViewController {

    private lazy var viewModel: RepositoriesListViewModel = Koin.instance
        .getRepositoriesListViewModel()

    private var repositories: [Repository] = []

    private let cellId = "repository"

    override func viewDidLoad() {
        super.viewDidLoad()
        setupNavigation()
        setupTableView()
        bindViewModel()
    }

    var onLogout: EmptyBlock?
    var showDetails: EmptyBlock?

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        navigationController?.setNavigationBarHidden(false, animated: animated)
    }

    private func setupNavigation() {
        navigationItem.hidesBackButton = true
        navigationItem.title = MR.strings().repositories.desc().localized()

        let button = UIBarButtonItem(
            image: R.image.ic_logout(),
            style: .plain,
            target: self,
            action: #selector(onLogoutTap)
        )

        button.tintColor = .white

        navigationItem.rightBarButtonItem = button
        navigationItem.backButtonTitle = ""
    }

    private func setupTableView() {
        tableView.register(
            UINib(nibName: "RepositoryItemCell", bundle: nil),
            forCellReuseIdentifier: cellId
        )

        tableView.rowHeight = UITableView.automaticDimension
        tableView.estimatedRowHeight = UITableView.automaticDimension
    }

    private func bindViewModel() {
        Task { [weak self] in
            guard let self = self else { return }
            for await state in self.viewModel.state {
                self.renderState(state)
            }
        }

        Task { [weak self] in
            guard let self = self else { return }
            for await action in self.viewModel.actions {
                self.handleAction(action)
            }
        }
    }

    private func renderState(_ state: RepositoriesListViewModelState) {
        switch state {
        case let loaded as RepositoriesListViewModelStateLoaded:
            repositories = loaded.repositories
            tableView.reloadData()

        default: break
        }
    }

    private func handleAction(_ action: RepositoriesListViewModelAction) {
        switch action {
        case is RepositoriesListViewModelActionLogout:
            onLogout?()

        case is RepositoriesListViewModelActionRouteToDetail:
            showDetails?()

        default: break
        }
    }

    @objc private func onLogoutTap() {
        viewModel.onLogoutButtonPressed()
    }
}

extension RepositoriesListViewController {

    override func tableView(
        _ tableView: UITableView,
        cellForRowAt indexPath: IndexPath
    ) -> UITableViewCell {
        guard
            let cell = tableView.dequeueReusableCell(
                withIdentifier: cellId,
                for: indexPath
            ) as? RepositoryItemCell
        else {
            fatalError("RepositoryItemCell not registered")
        }

        let repo = repositories[indexPath.row]

        cell.configure(
            name: repo.name,
            language: repo.language,
            description: repo.description_
        )

        return cell
    }

    override func tableView(
        _ tableView: UITableView,
        didSelectRowAt indexPath: IndexPath
    ) {
        // TODO: - viewModel.onSelectItem
        showDetails?()
    }
}

extension RepositoriesListViewController {

    override func tableView(
        _ tableView: UITableView,
        numberOfRowsInSection section: Int
    ) -> Int {
        return repositories.count
    }
}
