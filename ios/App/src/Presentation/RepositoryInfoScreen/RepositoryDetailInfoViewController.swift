import MarkdownKit
import MultiPlatformLibrary
import NVActivityIndicatorView
import UIKit

final class RepositoryDetailInfoViewController: UIViewController {
    @IBOutlet private var linkView: IconLabelView!
    @IBOutlet private var licenseView: IconLabelView!
    @IBOutlet private var licenseNameLabel: UILabel!
    @IBOutlet private var starsView: IconLabelView!
    @IBOutlet private var forksView: IconLabelView!
    @IBOutlet private var watchersView: IconLabelView!
    @IBOutlet private var readmeIndicator: NVActivityIndicatorView!
    @IBOutlet private var mainIndicator: NVActivityIndicatorView!
    @IBOutlet private var markdownTextView: UITextView!
    @IBOutlet private var placeholderView: PlaceholderView!

    private let owner: String
    private let repositoryName: String
    private let branch: String

    private lazy var viewModel: RepositoryInfoViewModel = Koin.instance
        .getRepositoryInfoViewModel(
            owner: owner,
            repositoryName: repositoryName,
            branch: branch
        )

    private var stateTask: Task<Void, Never>?
    private var actionTask: Task<Void, Never>?

    var onLogout: EmptyBlock?

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

    @available(*, unavailable)
    required init?(coder _: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        setupNavigation()
        setupIndicators()
        bindViewModel()
    }

    private func setupNavigation() {
        navigationItem.title = repositoryName

        let button = UIBarButtonItem(
            image: R.image.ic_logout(),
            style: .plain,
            target: self,
            action: #selector(onLogoutTap)
        )

        button.tintColor = .white

        navigationItem.rightBarButtonItem = button
    }

    private func setupIndicators() {
        mainIndicator.type = .circleStrokeSpin
        readmeIndicator.type = .circleStrokeSpin
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

    private func renderState(_ state: RepositoryInfoViewModelState) {
        placeholderView.isHidden = true

        switch state {
        case is RepositoryInfoViewModelStateLoading:
            handleLoadingState()

        case let loadedState as RepositoryInfoViewModelStateLoaded:
            handleLoadedState(
                details: loadedState.githubRepo,
                readmeState: loadedState.readmeState
            )

        case let state as RepositoryInfoViewModelStateError:
            handleErrorState(error: state.error)

        default: break
        }
    }

    private func handleLoadingState() {
        mainIndicator.startAnimating()
    }

    private func handleLoadedState(
        details: RepositoryDetails,
        readmeState: RepositoryInfoViewModelReadmeState
    ) {
        mainIndicator.stopAnimating()
        setupDetails(details: details)
        handleReadmeState(readmeState: readmeState)
    }

    private func setupDetails(details: RepositoryDetails) {
        linkView.configure(
            icon: R.image.ic_link(),
            title: details.url.toDisplayUrl(),
            titleColor: R.color.appBlue()!,
            additional: nil
        )

        linkView.onTap = {
            guard let url = URL(string: details.url) else { return }
            UIApplication.shared.open(url)
        }

        licenseView.configure(
            icon: R.image.ic_license(),
            title: R.string.localizable.license(),
            titleColor: .white,
            additional: nil
        )

        licenseNameLabel.text = details.license?.name ?? ""

        starsView.configure(
            icon: R.image.ic_star(),
            title: "\(details.stargazersCount)",
            titleColor: R.color.appYellow()!,
            additional: R.string.localizable.stars()
        )

        forksView.configure(
            icon: R.image.ic_fork(),
            title: "\(details.forksCount)",
            titleColor: R.color.appGreen()!,
            additional: R.string.localizable.forks()
        )

        watchersView.configure(
            icon: R.image.ic_watch(),
            title: "\(details.subscribersCount)",
            titleColor: R.color.appCyan()!,
            additional: R.string.localizable.watchers()
        )
    }

    private func handleReadmeState(
        readmeState: RepositoryInfoViewModelReadmeState
    ) {
        placeholderView.isHidden = true

        switch readmeState {
        case is RepositoryInfoViewModelReadmeStateLoading:
            readmeIndicator.startAnimating()

        case let state as RepositoryInfoViewModelReadmeStateLoaded:
            readmeIndicator.stopAnimating()
            handleMarkdown(markdownString: state.markdown)

        case is RepositoryInfoViewModelReadmeStateEmpty:
            readmeIndicator.stopAnimating()
            markdownTextView.text = R.string.localizable.no_readme_md()

        case let state as RepositoryInfoViewModelReadmeStateError:
            readmeIndicator.stopAnimating()
            handleErrorState(error: state.error)

        default: break
        }
    }

    private func handleMarkdown(markdownString: String?) {
        if let markdownString {
            let parser = MarkdownParser(
                font: UIFont.systemFont(ofSize: 16),
                color: R.color.white70()!
            )

            parser.enabledElements = [.header, .bold, .italic, .link]

            parser.header.font = UIFont.boldSystemFont(ofSize: 20)
            parser.header.color = .white

            markdownTextView.attributedText = parser.parse(markdownString)
        }
    }

    private func handleErrorState(error: ErrorModel) {
        placeholderView.isHidden = false

        placeholderView.configure(with: error) { [weak self] in
            self?.viewModel.onRetryButtonPressed()
        }
    }

    private func handleAction(_ action: RepositoryInfoViewModelAction) {
        switch action {
        case is RepositoryInfoViewModelActionLogout:
            onLogout?()
        default: break
        }
    }

    @objc private func onLogoutTap() {
        viewModel.onLogoutPressed()
    }

    deinit {
        stateTask?.cancel()
        actionTask?.cancel()
    }
}
