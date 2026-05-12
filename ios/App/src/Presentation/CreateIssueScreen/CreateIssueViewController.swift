import Down
import MultiPlatformLibrary
import NVActivityIndicatorView
import RxKeyboard
import RxSwift
import UIKit

final class CreateIssueViewController: BaseViewController {
    @IBOutlet private var scrollView: UIScrollView!
    @IBOutlet private var titleTextField: UITextField!
    @IBOutlet private var titleErrorLabel: UILabel!
    @IBOutlet private var bodyTextView: UITextView!
    @IBOutlet private var bodyErrorLabel: UILabel!
    @IBOutlet private var textViewHeightConstraint: NSLayoutConstraint!
    @IBOutlet private var attachFilesLabel: UILabel!
    @IBOutlet private var attachIndicator: NVActivityIndicatorView!
    @IBOutlet private var attachIcon: UIImageView!
    @IBOutlet private var attachedPhotosLabel: IconLabelView!
    @IBOutlet private var arrowIcon: UIImageView!
    @IBOutlet private var markdownView: UIView!
    @IBOutlet private var submitButton: LoadingButton!
    @IBOutlet private var bottomConstraint: NSLayoutConstraint!
    @IBOutlet private var markdownHeightConstraint: NSLayoutConstraint!

    private var downView: DownView?

    private let owner: String
    private let repositoryName: String

    private lazy var viewModel = Koin.instance
        .getCreateIssueViewModel(
            owner: owner,
            repositoryName: repositoryName
        )

    private var stateTask: Task<Void, Never>?
    private var actionTask: Task<Void, Never>?

    private let disposeBag = DisposeBag()

    var popViewController: EmptyBlock?
    var openImagePicker: EmptyBlock?

    init(
        owner: String,
        repositoryName: String,
    ) {
        self.owner = owner
        self.repositoryName = repositoryName
        super.init(nibName: "CreateIssueViewController", bundle: nil)
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
        localize()
        bindInputs()
        bindViewModel()
        bindKeyboard()
    }

    private func setupNavigation() {
        navigationItem.title = R.string.localizable.new_issue()
    }

    private func setupUI() {
        scrollView.showsVerticalScrollIndicator = false
        scrollView.showsHorizontalScrollIndicator = false

        titleTextField.delegate = self
        bodyTextView.delegate = self

        submitButton.configure(style: .primary)

        let attachGesture = UITapGestureRecognizer(
            target: self,
            action: #selector(onAttachTap)
        )
        attachFilesLabel.addGestureRecognizer(attachGesture)

        attachIndicator.type = .circleStrokeSpin
        attachIndicator.startAnimating()

        let arrowGesture = UITapGestureRecognizer(
            target: self,
            action: #selector(onArrowTap)
        )
        arrowIcon.addGestureRecognizer(arrowGesture)
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
        downView.scrollView.isScrollEnabled = false

        markdownView.addSubview(downView)

        NSLayoutConstraint.activate([
            downView.topAnchor.constraint(equalTo: markdownView.topAnchor),
            downView.leadingAnchor.constraint(equalTo: markdownView.leadingAnchor),
            downView.trailingAnchor.constraint(equalTo: markdownView.trailingAnchor),
            downView.bottomAnchor.constraint(equalTo: markdownView.bottomAnchor),
        ])

        self.downView = downView
    }

    private func localize() {
        let titlePlaceholder = R.string.localizable.title()
        titleTextField.setupBorderedField(placeholder: titlePlaceholder)

        let bodyPlaceholder = R.string.localizable.description()
        bodyTextView.setupBorderedField(placeholder: bodyPlaceholder)

        attachFilesLabel.text = R.string.localizable.attach_files()

        attachedPhotosLabel.configure(
            icon: R.image.ic_img(),
            title: R.string.localizable.zero(),
            titleColor: R.color.appBlue()!,
            additional: R.string.localizable.attached_photos()
        )

        submitButton.setTitle(
            R.string.localizable.submit_new_issue_button_title(),
            for: .normal
        )
    }

    private func bindInputs() {
        _ = titleTextField.bindTextTwoWay(liveData: viewModel.title.data)
        _ = titleErrorLabel.bindText(liveData: viewModel.title.error)
        _ = titleErrorLabel.bindHidden(liveData: viewModel.title.isValid)

        _ = bodyTextView.bindTextTwoWay(liveData: viewModel.body.data)
        _ = bodyErrorLabel.bindText(liveData: viewModel.body.error)
        _ = bodyErrorLabel.bindHidden(liveData: viewModel.body.isValid)
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

    private func bindKeyboard() {
        RxKeyboard.instance.visibleHeight
            .drive(onNext: { [weak self] keyboardHeight in
                guard let self else { return }

                self.bottomConstraint.constant = keyboardHeight + 16
                self.view.layoutIfNeeded()
            })
            .disposed(by: disposeBag)
    }

    private func renderState(_ state: CreateIssueViewModel.State) {
        let isLoading = state.isLoading

        isLoading
            ? submitButton.startAnimating()
            : submitButton.stopAnimating()

        submitButton.isEnabled = !isLoading
        submitButton.setTitleColor(
            isLoading ? UIColor.white.withAlphaComponent(0) : .white,
            for: .normal
        )

        bodyTextView.replace(
            bodyTextView.selectedTextRange ?? UITextRange(),
            withText: ""
        )

        renderAttachments(state: state)
    }

    private func renderAttachments(state: CreateIssueViewModel.State) {
        let isUploading = state.totalCount > 0 && state.uploadingCount < state.totalCount

        if isUploading {
            let counter = "\(state.uploadingCount)/\(state.totalCount)"
            attachFilesLabel.text = R.string.localizable.upload_files() + counter
        } else {
            attachFilesLabel.text = R.string.localizable.attach_files()
        }

        attachIndicator.isHidden = !isUploading
        attachIcon.isHidden = isUploading

        let angle: CGFloat = state.isExpanded ? .pi : 0
        UIView.animate(withDuration: 0.3, animations: {
            self.arrowIcon.transform = CGAffineTransform(rotationAngle: angle)
        }) { _ in
            self.markdownView.isHidden = !state.isExpanded
        }

        attachedPhotosLabel.configure(
            icon: R.image.ic_img(),
            title: "\(state.uploadingCount)",
            titleColor: R.color.appBlue()!,
            additional: R.string.localizable.attached_photos()
        )

        renderUploadedUrls(urls: state.uploadedUrls)
    }

    private func renderUploadedUrls(urls: [String]) {
        let markdown = urls
            .map { "![image](\($0))" }
            .joined(separator: "\n\n")

        updateMarkdown(markdownString: markdown)
    }

    private func handleAction(_ action: CreateIssueViewModelAction) {
        switch onEnum(of: action) {
        case let .showError(error):
            showErrorAlert(error: error.error)
        case .routeBack:
            popViewController?()
        case .openImagePicker:
            openImagePicker?()
        }
    }

    private func updateMarkdown(markdownString: String?) {
        do {
            try downView?.update(
                markdownString: markdownString ?? "",
                options: nil,
                didLoadSuccessfully: {
                    DispatchQueue.main.asyncAfter(
                        deadline: .now() + 0.1,
                        execute: { [weak self] in
                            let contentHeight =
                                self?.downView?.scrollView.contentSize.height ?? 0
                            self?.markdownHeightConstraint.constant = contentHeight
                        }
                    )
                }
            )
        } catch {
            assertionFailure("DownView update failed: \(error)")
        }
    }

    func onFilesSelected(_ data: [KotlinByteArray]) {
        viewModel.onFilesSelected(files: data)
    }

    @IBAction private func onSubmitButtonTap(_: Any) {
        view.endEditing(true)
        viewModel.onSubmitButtonPressed()
    }

    @objc private func onAttachTap() {
        viewModel.onAttachPressed()
    }

    @objc private func onArrowTap() {
        viewModel.onArrowPressed()
    }

    deinit {
        stateTask?.cancel()
        actionTask?.cancel()
    }
}

extension CreateIssueViewController: UITextFieldDelegate {
    func textFieldDidBeginEditing(_: UITextField) {
        titleTextField.layer.borderColor = R.color.appBlue()!.cgColor
        titleErrorLabel.text = nil
    }

    func textFieldDidEndEditing(_: UITextField) {
        if viewModel.title.validate() {
            titleTextField.layer.borderColor = R.color.appGrey()!.cgColor
        } else {
            titleTextField.layer.borderColor = R.color.appError()!.cgColor
        }
    }

    func textFieldShouldReturn(_: UITextField) -> Bool {
        bodyTextView.becomeFirstResponder()
        return true
    }
}

extension CreateIssueViewController: UITextViewDelegate {
    func textViewDidChange(_ textView: UITextView) {
        let size = textView.sizeThatFits(
            CGSize(
                width: textView.frame.width,
                height: .greatestFiniteMagnitude
            )
        )

        textViewHeightConstraint.constant = max(48, size.height)
    }

    func textViewDidBeginEditing(_: UITextView) {
        bodyTextView.layer.borderColor = R.color.appBlue()!.cgColor
        bodyErrorLabel.text = nil
    }

    func textViewDidEndEditing(_: UITextView) {
        if viewModel.body.validate() {
            bodyTextView.layer.borderColor = R.color.appGrey()!.cgColor
        } else {
            bodyTextView.layer.borderColor = R.color.appError()!.cgColor
        }
    }
}
