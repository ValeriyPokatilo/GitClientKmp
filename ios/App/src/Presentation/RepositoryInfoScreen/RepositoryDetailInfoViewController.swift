import Down
import MultiPlatformLibrary
import NVActivityIndicatorView
import UIKit

final class RepositoryDetailInfoViewController: BaseViewController {
    @IBOutlet private var linkView: IconLabelView!
    @IBOutlet private var licenseView: IconLabelView!
    @IBOutlet private var licenseNameLabel: UILabel!
    @IBOutlet private var starsView: IconLabelView!
    @IBOutlet private var forksView: IconLabelView!
    @IBOutlet private var watchersView: IconLabelView!
    @IBOutlet private var issueView: IconLabelView!
    @IBOutlet private var issueLinkLabel: UILabel!
    @IBOutlet private var readmeIndicator: NVActivityIndicatorView!
    @IBOutlet private var markdownView: UIView!
    @IBOutlet private var refreshButton: LoadingButton!

    private var downView: DownView?

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
    var onViewIssues: EmptyBlock?

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
        setupUI()
        setupDown()
        bindViewModel()

        viewModel.onStart()
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
        navigationItem.backButtonTitle = ""
    }

    private func setupUI() {
        readmeIndicator.type = .circleStrokeSpin
        refreshButton.configure(style: .primary)
    }

    private func setupDown() {
        guard let downView = try? DownView(
            frame: .zero,
            markdownString: "",
            templateBundle: Bundle.main
        ) else {
            return
        }

        downView.backgroundColor = R.color.appBackground()!
        downView.translatesAutoresizingMaskIntoConstraints = false
        downView.scrollView.showsHorizontalScrollIndicator = false
        downView.scrollView.alwaysBounceHorizontal = false

        markdownView.addSubview(downView)

        NSLayoutConstraint.activate([
            downView.topAnchor.constraint(equalTo: markdownView.topAnchor),
            downView.leadingAnchor.constraint(equalTo: markdownView.leadingAnchor),
            downView.trailingAnchor.constraint(equalTo: markdownView.trailingAnchor),
            downView.bottomAnchor.constraint(equalTo: markdownView.bottomAnchor),
        ])

        self.downView = downView
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
        renderLoading(state)
        renderPlaceholder(state)
        renderContent(state)
    }

    private func renderLoading(_ state: RepositoryInfoViewModelState) {
        let isLoading = state is RepositoryInfoViewModelStateLoading

        isLoading
            ? startLoading()
            : stopLoading()
    }

    private func renderPlaceholder(_ state: RepositoryInfoViewModelState) {
        switch onEnum(of: state) {
        case let .error(errorState):
            showErrorPlaceholder(error: errorState.error)
            refreshButton.isHidden = false
            refreshButton.setTitle(
                R.string.localizable.retry(),
                for: .normal
            )
        default:
            hidePlaceholder()
            refreshButton.isHidden = true
        }
    }

    private func renderContent(_ state: RepositoryInfoViewModelState) {
        guard let loadedState = state as? RepositoryInfoViewModelStateLoaded
        else {
            return
        }

        stopLoading()
        setupDetails(details: loadedState.githubRepo)
        renderReadmeState(loadedState.readmeState)
    }

    private func renderReadmeState(
        _ readmeState: RepositoryInfoViewModelReadmeState
    ) {
        renderReadmeLoading(readmeState)

        switch onEnum(of: readmeState) {
        case let .loaded(loadedState):
            markdownView.isHidden = false
            handleMarkdown(markdownString: loadedState.markdown)
        case .empty:
            markdownView.isHidden = false
            handleMarkdown(
                markdownString: R.string.localizable.no_readme_md()
            )
        case let .error(errorState):
            markdownView.isHidden = true
            handleErrorState(error: errorState.error)

        default:
            break
        }
    }

    private func renderReadmeLoading(
        _ readmeState: RepositoryInfoViewModelReadmeState
    ) {
        let isLoading = readmeState is RepositoryInfoViewModelReadmeStateLoading

        isLoading
            ? readmeIndicator.startAnimating()
            : readmeIndicator.stopAnimating()

        markdownView.isHidden =
            isLoading || readmeState is RepositoryInfoViewModelReadmeStateError
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

        issueView.configure(
            icon: R.image.ic_issue(),
            title: "\(details.openIssuesCount)",
            titleColor: R.color.appLightGreen()!,
            additional: R.string.localizable.issues()
        )

        let attributeString = NSMutableAttributedString(
            string: R.string.localizable.view_issues()
        )
        attributeString.addAttribute(
            .underlineStyle,
            value: NSUnderlineStyle.single.rawValue,
            range: NSRange(location: 0, length: attributeString.length)
        )

        issueLinkLabel.attributedText = attributeString
        let tapGesture = UITapGestureRecognizer(
            target: self,
            action: #selector(onViewIssuesTap)
        )
        issueLinkLabel.addGestureRecognizer(tapGesture)
    }

    private func handleMarkdown(markdownString: String?) {
        do {
            try downView?.update(
                markdownString: markdownString ?? "",
                options: nil,
                didLoadSuccessfully: nil
            )
        } catch {
            assertionFailure("DownView update failed: \(error)")
        }
    }

    private func handleErrorState(error: ErrorModel) {
        showErrorPlaceholder(error: error)
        refreshButton.isHidden = false
        refreshButton.setTitle(
            R.string.localizable.retry(),
            for: .normal
        )
    }

    private func handleAction(_ action: RepositoryInfoViewModelAction) {
        switch onEnum(of: action) {
        case .logout:
            onLogout?()
        case .routeToIssues:
            onViewIssues?()
        default: break
        }
    }

    @objc func onViewIssuesTap() {
        viewModel.onViewIssuesPressed()
    }

    @objc private func onLogoutTap() {
        viewModel.onLogoutPressed()
    }

    deinit {
        stateTask?.cancel()
        actionTask?.cancel()
    }
}
