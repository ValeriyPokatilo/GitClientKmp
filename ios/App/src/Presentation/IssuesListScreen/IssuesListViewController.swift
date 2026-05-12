import MultiPlatformLibrary
import MultiPlatformLibraryUnits
import NVActivityIndicatorView
import UIKit

final class IssuesListViewController: BaseViewController {
    @IBOutlet private var tableView: UITableView!
    @IBOutlet private var createIssueButton: LoadingButton!
    @IBOutlet private var refreshButton: LoadingButton!

    private let owner: String
    private let repositoryName: String

    private lazy var viewModel: IssuesListViewModel = Koin.instance
        .getIssuesListViewModel(
            owner: owner,
            repositoryName: repositoryName
        )

    private lazy var dataSource: TableUnitsSource = TableUnitsSourceKt.default(
        for: tableView
    )

    private var actionTask: Task<Void, Never>?

    var showCreateIssue: EmptyBlock?
    var showDetails: ParameterBlock<Int>?

    init(
        owner: String,
        repositoryName: String,
    ) {
        self.owner = owner
        self.repositoryName = repositoryName
        super.init(nibName: "IssuesListViewController", bundle: nil)
    }

    @available(*, unavailable)
    required init?(coder _: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        setupNavigation()
        localize()
        setupUI()
        setupTableView()
        bindViewModel()

        viewModel.onStart()
    }

    private func setupNavigation() {
        navigationItem.title = R.string.localizable.issues()
        navigationItem.backButtonTitle = ""
    }

    private func localize() {
        createIssueButton.setTitle(
            R.string.localizable.new_issue_button_title(),
            for: .normal
        )
    }

    private func setupUI() {
        createIssueButton.configure(style: .primary)
        refreshButton.configure(style: .secondary)
    }

    private func setupTableView() {
        tableView.rowHeight = UITableView.automaticDimension
        tableView.estimatedRowHeight = UITableView.automaticDimension
        tableView.separatorStyle = .singleLine
    }

    private func bindViewModel() {
        viewModel.state.addObserver { [weak self] state in
            guard let self, let state else { return }
            self.bindState(state)
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

    private func bindState(
        _ state: ResourceState<NSArray, ErrorModel>
    ) {
        renderLoading(state)
        renderPlaceholder(state)
        renderContent(state)
        renderButtons(state)
    }

    private func renderLoading(_ state: ResourceState<NSArray, ErrorModel>) {
        let isLoading = state is ResourceStateLoading

        isLoading
            ? startLoading()
            : stopLoading()
    }

    private func renderPlaceholder(_ state: ResourceState<NSArray, ErrorModel>) {
        switch onEnum(of: state) {
        case .empty:
            stopLoading()
            showEmptyIssues()
            refreshButton.isHidden = false
            refreshButton.setTitle(R.string.localizable.refresh(), for: .normal)
        case let .failed(errorState):
            stopLoading()
            refreshButton.isHidden = false
            refreshButton.setTitle(R.string.localizable.retry(), for: .normal)
            if let error = errorState.error {
                showErrorPlaceholder(error: error)
            }
        default:
            hidePlaceholder()
        }
    }

    private func renderContent(_ state: ResourceState<NSArray, ErrorModel>) {
        switch onEnum(of: state) {
        case let .success(dataState):
            let data = dataState.data ?? []

            let items: [IssuesListViewModelUiItem] = data.compactMap {
                $0 as? IssuesListViewModelUiItem
            }

            tableView.isHidden = items.isEmpty
            bindItems(items)
        default:
            tableView.isHidden = true
            bindItems([])
        }
    }

    private func renderButtons(_ state: ResourceState<NSArray, ErrorModel>) {
        let isLoading = state is ResourceStateLoading

        createIssueButton.isHidden = isLoading

        refreshButton.isHidden = isLoading ||
            !(state is ResourceStateEmpty || state is ResourceStateFailed)
    }

    private func bindItems(_ items: [IssuesListViewModelUiItem]) {
        let units: [TableUnitItem] = items.map {
            mapUnitItem($0)
        }

        dataSource.unitItems = units
    }

    private func handleAction(_ action: IssuesListViewModelAction) {
        switch onEnum(of: action) {
        case .routeToCreate:
            showCreateIssue?()
        case let .routeToDetail(details):
            showDetails?(Int(details.issueNumber))
        default: break
        }
    }

    private func mapUnitItem(
        _ item: IssuesListViewModelUiItem
    ) -> TableUnitItem {
        switch onEnum(of: item) {
        case let .issueItem(issue):
            return UITableViewCellUnit<IssueItemCell>(
                data: IssueItemCell.Data(issue: issue.issue) { [weak self] selected in
                    self?.viewModel.onIssueItemPressed(issue: selected)
                },
                itemId: Int64(issue.issue.id),
            )
        case .loaderItem:
            return UITableViewCellUnit<LoaderTableCell>(
                data: (),
                itemId: TableUnitItemCompanion.shared.NO_ID
            )
        }
    }

    @IBAction private func onCreateIssueButtonTap(_: Any) {
        viewModel.onCreateIssuePressed()
    }

    @IBAction func onRefreshButtonTap(_: Any) {
        viewModel.onRetryButtonPressed()
    }

    deinit {
        actionTask?.cancel()
    }
}

extension IssuesListViewController: UITableViewDelegate {
    func tableView(
        _: UITableView,
        willDisplay _: UITableViewCell,
        forRowAt indexPath: IndexPath
    ) {
        guard indexPath.row + 1 == tableView.dataSource?.tableView(
            tableView,
            numberOfRowsInSection: indexPath.section
        ) else { return }

        viewModel.onReachEnd()
    }
}
