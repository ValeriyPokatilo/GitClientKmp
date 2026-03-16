import MultiPlatformLibrary
import NVActivityIndicatorView
import UIKit

final class RepositoriesListViewController: UITableViewController {

    private lazy var viewModel: RepositoriesListViewModel = Koin.instance
        .getRepositoriesListViewModel()

    private var repositories: [Repository] = []

    private let cellId = "repository"

    private let indicatorView = NVActivityIndicatorView(
        frame: .zero,
        type: .circleStrokeSpin,
        color: .white,
        padding: 0
    )

    private let placeholderView = PlaceholderView()

    private let indicatorViewSize: CGFloat = 56

    var onLogout: EmptyBlock?
    var showDetails: ParameterBlock<(String, String, String)>?

    override func viewDidLoad() {
        super.viewDidLoad()
        setupNavigation()
        setupIndicator()
        setupTableView()
        bindViewModel()
    }

    private func setupNavigation() {
        navigationController?.setNavigationBarHidden(false, animated: false)
        
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

    private func setupIndicator() {
        view.addSubview(indicatorView)
        indicatorView.translatesAutoresizingMaskIntoConstraints = false
        NSLayoutConstraint.activate([
            indicatorView.centerXAnchor.constraint(
                equalTo: view.safeAreaLayoutGuide.centerXAnchor
            ),
            indicatorView.centerYAnchor.constraint(
                equalTo: view.safeAreaLayoutGuide.centerYAnchor
            ),
            indicatorView.widthAnchor.constraint(
                equalToConstant: indicatorViewSize
            ),
            indicatorView.heightAnchor.constraint(
                equalToConstant: indicatorViewSize
            ),
        ])
    }

    private func bindViewModel() {
        Task { [weak self] in
            guard let self = self else { return }
            for await state in self.viewModel.state {
                await MainActor.run {
                    self.renderState(state)
                }
            }
        }

        Task { [weak self] in
            guard let self = self else { return }
            for await action in self.viewModel.actions {
                await MainActor.run {
                    self.handleAction(action)
                }
            }
        }
    }

    private func renderState(_ state: RepositoriesListViewModelState) {
        switch state {
        case is RepositoriesListViewModelStateLoading:
            handleLoadingState()

        case let loaded as RepositoriesListViewModelStateLoaded:
            handleLoadedState(items: loaded.repositories)

        case is RepositoriesListViewModelStateEmpty:
            handleEmptyState()

        case let errorState as RepositoriesListViewModelStateError:
            handleErrorState(error: errorState.error)

        default: break
        }
    }

    private func handleLoadingState() {
        indicatorView.startAnimating()
        tableView.backgroundView = nil
        repositories.removeAll()
        tableView.reloadData()
    }

    private func handleLoadedState(items: [Repository]) {
        indicatorView.stopAnimating()
        repositories = items
        tableView.backgroundView = nil
        tableView.reloadData()
    }

    private func handleEmptyState() {
        indicatorView.stopAnimating()
        placeholderView.configure(
            image: R.image.ic_empty(),
            title: MR.strings().repositories_empty_title.desc().localized(),
            message: MR.strings().repositories_empty_message.desc().localized(),
            isError: false,
            action: { [weak self] in
                self?.viewModel.onRetryButtonPressed()
            }
        )
        tableView.backgroundView = placeholderView
    }

    private func handleErrorState(error: AppError) {
        indicatorView.stopAnimating()

        let title: String
        let message: String
        let icon: UIImage?

        switch error {
        case let httpError as AppError.Http:
            title = "\(httpError.code)"
            message = httpError.message ?? ""
            icon = R.image.ic_error()

        case is AppError.Network:
            title = MR.strings().repositories_connection_error_title.desc()
                .localized()
            message = MR.strings().repositories_connection_error_message.desc()
                .localized()
            icon = R.image.ic_connection_error()

        default:
            title = ""
            message = ""
            icon = nil
        }

        placeholderView.configure(
            image: icon,
            title: title,
            message: message,
            isError: true,
            action: { [weak self] in
                self?.viewModel.onRetryButtonPressed()
            }
        )

        tableView.backgroundView = placeholderView
    }

    private func handleAction(_ action: RepositoriesListViewModelAction) {
        switch action {
        case is RepositoriesListViewModelActionLogout:
            onLogout?()

        case let details as RepositoriesListViewModelActionRouteToDetail:
            showDetails?((details.owner, details.repositoryName, details.branch))

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
        tableView.deselectRow(at: indexPath, animated: true)
        viewModel.onRepositoryItemPressed(repository: repositories[indexPath.row])
    }

    override func tableView(
        _ tableView: UITableView,
        numberOfRowsInSection section: Int
    ) -> Int {
        return repositories.count
    }
}
