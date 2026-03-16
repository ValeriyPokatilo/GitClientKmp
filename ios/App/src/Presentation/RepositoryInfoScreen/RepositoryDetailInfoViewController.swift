import MultiPlatformLibrary
import NVActivityIndicatorView
import UIKit

final class RepositoryDetailInfoViewController: UIViewController {

    @IBOutlet private weak var linkView: IconLabelView!
    @IBOutlet private weak var licenseView: IconLabelView!
    @IBOutlet private weak var licenseNameLabel: UILabel!
    @IBOutlet private weak var starsView: IconLabelView!
    @IBOutlet private weak var forksView: IconLabelView!
    @IBOutlet private weak var watchersView: IconLabelView!
    @IBOutlet private weak var activityIndicator: NVActivityIndicatorView!

    private let owner: String
    private let repositoryName: String
    private let branch: String

    private lazy var viewModel: RepositoryInfoViewModel = Koin.instance
        .getRepositoryInfoViewModel(
            owner: owner,
            repositoryName: repositoryName,
            branch: branch
        )

    private let placeholderView = PlaceholderView()

    var onLogout: EmptyBlock?
    var onGoBack: EmptyBlock?

    init(
        owner: String,
        repositoryName: String,
        branch: String
    ) {
        self.owner = owner
        self.repositoryName = repositoryName
        self.branch = branch
        super.init(nibName: "RepositoryDetailInfoViewController", bundle: nil)
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        setupNavigation()
        setupIndicator()
        bindViewModel()
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

    private func setupIndicator() {
        activityIndicator.type = .circleStrokeSpin
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

    private func renderState(_ state: RepositoryInfoViewModelState) {
        switch state {
        case is RepositoryInfoViewModelStateLoading:
            handleLoadingState()
        case let loadedState as RepositoryInfoViewModelStateLoaded:
            handleLoadedState(
                details: loadedState.githubRepo,
                readmeState: loadedState.readmeState
            )
        case let errorState as RepositoryInfoViewModelStateError:
            handleErrorState(error: errorState.error)
        default: break
        }
    }

    private func handleLoadingState() {
        activityIndicator.startAnimating()
    }

    private func handleLoadedState(
        details: RepositoryDetails,
        readmeState: RepositoryInfoViewModelReadmeState
    ) {
        activityIndicator.stopAnimating()
        setupDetails(details: details)
    }

    private func setupDetails(details: RepositoryDetails) {
        linkView.configure(
            icon: R.image.ic_link(),
            title: details.url,
            titleColor: R.color.appBlue() ?? UIColor(),
            additional: nil
        )

        licenseView.configure(
            icon: R.image.ic_license(),
            title: MR.strings().license.desc().localized(),
            titleColor: R.color.white() ?? UIColor(),
            additional: nil
        )

        licenseNameLabel.text = details.license?.name ?? ""

        starsView.configure(
            icon: R.image.ic_star(),
            title: "\(details.stargazersCount)",
            titleColor: R.color.appYellow() ?? UIColor(),
            additional: MR.strings().stars.desc().localized()
        )

        forksView.configure(
            icon: R.image.ic_fork(),
            title: "\(details.forksCount)",
            titleColor: R.color.appGreen() ?? UIColor(),
            additional: MR.strings().forks.desc().localized()
        )

        watchersView.configure(
            icon: R.image.ic_watch(),
            title: "\(details.subscribersCount)",
            titleColor: R.color.appCyan() ?? UIColor(),
            additional: MR.strings().watchers.desc().localized()
        )
    }

    private func handleReadmeState(
        readmeState: RepositoryInfoViewModelReadmeState
    ) {

    }

    private func handleErrorState(error: AppError) {

    }

    private func handleAction(_ action: RepositoryInfoViewModelAction) {
        switch action {
        case is RepositoryInfoViewModelActionLogout:
            onLogout?()

        case is RepositoryInfoViewModelActionRouteBack:
            onGoBack?()

        default: break
        }
    }

    @objc private func onLogoutTap() {
        viewModel.onLogoutPressed()
    }
}
