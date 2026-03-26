import MultiPlatformLibrary
import MultiPlatformLibraryUnits
import NVActivityIndicatorView
import UIKit

final class RepositoriesListViewController: UIViewController {
    @IBOutlet private var tableView: UITableView!
    @IBOutlet private var indicatorView: NVActivityIndicatorView!
    @IBOutlet private var placeholderView: PlaceholderView!

    private lazy var viewModel: RepositoriesListViewModel = Koin.instance
        .getRepositoriesListViewModel()

    private var stateTask: Task<Void, Never>?
    private var actionTask: Task<Void, Never>?

    private lazy var dataSource: TableUnitsSource = TableUnitsSourceKt.default(
        for: tableView
    )

    var logout: EmptyBlock?
    var showDetails: ParameterBlock<RepositoryDetailsRoute>?

    override func viewDidLoad() {
        super.viewDidLoad()
        localize()
        setupNavigation()
        setupIndicator()
        setupTableView()
        bindViewModel()
    }

    private func setupIndicator() {
        indicatorView.type = .circleStrokeSpin
    }

    private func setupTableView() {
        tableView.rowHeight = UITableView.automaticDimension
        tableView.estimatedRowHeight = UITableView.automaticDimension
        tableView.separatorStyle = .singleLine
    }

    private func setupNavigation() {
        navigationController?.setNavigationBarHidden(false, animated: false)

        navigationItem.hidesBackButton = true

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

    private func localize() {
        navigationItem.title = R.string.localizable.repositories()
    }

    private func bindViewModel() {
        stateTask = Task { [weak self] in
            guard let self else { return }
            for await state in self.viewModel.state {
                await MainActor.run {
                    self.renderState(state)
                }
            }
        }

        actionTask = Task { [weak self] in
            guard let self else { return }
            for await action in self.viewModel.action {
                await MainActor.run {
                    self.handleAction(action)
                }
            }
        }
    }

    private func renderState(_ state: RepositoriesListViewModelState) {
        let isLoading = state is RepositoriesListViewModelStateLoading
        let isLoaded = state is RepositoriesListViewModelStateLoaded
        let isEmpty = state is RepositoriesListViewModelStateEmpty
        let isError = state is RepositoriesListViewModelStateError

        if isLoading {
            indicatorView.startAnimating()
        } else {
            indicatorView.stopAnimating()
        }

        tableView.isHidden = !isLoaded

        placeholderView.isHidden = isLoaded || isLoading
        if let emptyState = state as? RepositoriesListViewModelStateEmpty {
            placeholderView.configureEmpty { [weak self] in
                self?.viewModel.onRetryButtonPressed()
            }
        } else if let errorState = state as? RepositoriesListViewModelStateError {
            placeholderView.configure(with: errorState.error) { [weak self] in
                self?.viewModel.onRetryButtonPressed()
            }
        } else {
            placeholderView.isHidden = true
        }

        if let loadedState = state as? RepositoriesListViewModelStateLoaded {
            let units = loadedState.repositories.map { repo in
                repo.toTableUnitItem { [weak self] selected in
                    self?.viewModel.onRepositoryItemPressed(
                        repository: selected
                    )
                }
            }
            dataSource.unitItems = units
        } else {
            dataSource.unitItems = []
        }
    }

    private func handleAction(_ action: RepositoriesListViewModelAction) {
        switch action {
        case is RepositoriesListViewModelActionLogout:
            logout?()

        case let details as RepositoriesListViewModelActionRouteToDetail:
            showDetails?(
                RepositoryDetailsRoute(
                    owner: details.owner,
                    repositoryName: details.repositoryName,
                    branch: details.branch
                )
            )

        default: break
        }
    }

    @objc private func onLogoutTap() {
        viewModel.onLogoutButtonPressed()
    }

    deinit {
        stateTask?.cancel()
        actionTask?.cancel()
    }
}
