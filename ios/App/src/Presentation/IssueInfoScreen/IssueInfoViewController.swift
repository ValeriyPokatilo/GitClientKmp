import Down
import MultiPlatformLibrary
import UIKit

final class IssueInfoViewController: BaseViewController {
    @IBOutlet private var statusLabel: ChipLabel!
    @IBOutlet private var updatedAtLabel: ChipLabel!
    @IBOutlet private var titleLabel: UILabel!
    @IBOutlet private var descriptionLabel: UILabel!
    @IBOutlet private var markdownView: UIView!
    @IBOutlet private var retryButton: LoadingButton!

    private var downView: DownView?

    private let owner: String
    private let repositoryName: String
    private let issueNumber: Int

    private lazy var viewModel: IssueInfoViewModel = Koin.instance
        .getIssueInfoViewModel(
            owner: owner,
            repositoryName: repositoryName,
            issueNumber: Int32(issueNumber)
        )

    private var stateTask: Task<Void, Never>?

    init(
        owner: String,
        repositoryName: String,
        issueNumber: Int
    ) {
        self.owner = owner
        self.repositoryName = repositoryName
        self.issueNumber = issueNumber
        super.init(nibName: "IssueInfoViewController", bundle: nil)
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

    private func setupUI() {
        retryButton.configure(style: .primary)
        retryButton.setTitle(R.string.localizable.retry(), for: .normal)
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

    private func setupNavigation() {
        let title = "\(R.string.localizable.issue_number())\(issueNumber)"
        navigationItem.title = title
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
    }

    private func renderState(_ state: IssueInfoViewModelState) {
        renderLoading(state)
        renderPlaceholder(state)
        renderContent(state)
    }

    private func renderLoading(_ state: IssueInfoViewModelState) {
        let isLoading = state is IssueInfoViewModelStateLoading

        isLoading
            ? startLoading()
            : stopLoading()

        setContentHidden(isLoading)

        if isLoading {
            retryButton.isHidden = true
        }
    }

    private func renderPlaceholder(_ state: IssueInfoViewModelState) {
        guard let errorState = state as? IssueInfoViewModelStateError else {
            hidePlaceholder()
            retryButton.isHidden = true
            return
        }

        stopLoading()
        setContentHidden(true)

        showErrorPlaceholder(error: errorState.error)
        retryButton.isHidden = false
    }

    private func renderContent(_ state: IssueInfoViewModelState) {
        guard let loadedState = state as? IssueInfoViewModelStateLoaded else {
            return
        }

        hidePlaceholder()
        retryButton.isHidden = true
        setContentHidden(false)

        let issue = loadedState.issue

        switch issue.state {
        case .open:
            statusLabel.configure(
                text: issue.state.displayText(),
                textColor: R.color.appLightGreen()!,
                backgroundColor: R.color.appLightGreen()!.withAlphaComponent(0.2)
            )
        case .closed:
            statusLabel.configure(
                text: issue.state.displayText(),
                textColor: R.color.appError()!,
                backgroundColor: R.color.appError()!.withAlphaComponent(0.2)
            )
        }

        updatedAtLabel.configure(
            text: issue.updatedAt.toShortDate(),
            textColor: R.color.white50()!,
            backgroundColor: UIColor.lightGray.withAlphaComponent(0.2)
        )

        titleLabel.text = issue.title
        handleMarkdown(markdownString: issue.body)
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

    private func setContentHidden(_ isHidden: Bool) {
        statusLabel.isHidden = isHidden
        updatedAtLabel.isHidden = isHidden
        titleLabel.isHidden = isHidden
        descriptionLabel.isHidden = isHidden
        markdownView.isHidden = isHidden
    }

    @IBAction private func onRetryButtonTap(_: Any) {
        viewModel.onRetryButtonPressed()
    }

    deinit {
        stateTask?.cancel()
    }
}
