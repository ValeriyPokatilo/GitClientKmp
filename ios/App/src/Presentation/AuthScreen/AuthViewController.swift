import MultiPlatformLibrary
import NVActivityIndicatorView
import RswiftResources
import RxKeyboard
import RxSwift
import UIKit

final class AuthViewController: UIViewController {
    @IBOutlet private var mainLogoImageView: UIImageView!
    @IBOutlet private var tokenTextField: UITextField!
    @IBOutlet private var errorLabel: UILabel!
    @IBOutlet private var signInButton: UIButton!
    @IBOutlet private var bottomConstraint: NSLayoutConstraint!
    @IBOutlet private var indicatorView: NVActivityIndicatorView!

    private lazy var viewModel: AuthViewModel = Koin.instance.getAuthViewModel()

    private let disposeBag = DisposeBag()

    private var stateTask: Task<Void, Never>?
    private var actionTask: Task<Void, Never>?

    var routeToMain: EmptyBlock?
    var showAlert: ParameterBlock<AlertModel>?

    override func viewDidLoad() {
        super.viewDidLoad()
        setupUI()
        localize()
        bindKeyboard()
        bindViewModel()
    }

    private func setupUI() {
        navigationController?.setNavigationBarHidden(true, animated: false)

        mainLogoImageView.image = R.image.main_logo()

        indicatorView.type = .circleStrokeSpin
    }

    private func localize() {
        let placeholder = R.string.localizable.token_text_field_placeholder()
        tokenTextField.setupBorderedField(placeholder: placeholder)

        signInButton.setTitle(
            R.string.localizable.sign_in_button_title(),
            for: .normal
        )
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

    private func bindViewModel() {
        tokenTextField.rx.text.orEmpty
            .subscribe(onNext: { [weak self] text in
                self?.viewModel.onTokenChanged(text: text)
            })
            .disposed(by: disposeBag)

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

    private func renderState(_ state: AuthViewModelState) {
        let isLoading = state is AuthViewModelStateLoading
        let isInvalid = state is AuthViewModelStateInvalidInput

        errorLabel.isHidden = !isInvalid
        errorLabel.text = isInvalid
            ? R.string.localizable.invalid_token_reason()
            : nil

        signInButton.isEnabled = !isLoading && !isInvalid
        signInButton.titleLabel?.isHidden = isLoading

        let borderColor = isInvalid
            ? UIColor.red
            : R.color.appGrey()!
        tokenTextField.layer.borderColor = borderColor.cgColor

        isLoading
            ? indicatorView.startAnimating()
            : indicatorView.stopAnimating()
    }

    private func handleAction(_ action: AuthViewModelAction) {
        switch onEnum(of: action) {
        case .routeToMain:
            routeToMain?()
        case let .showError(errorState):
            showErrorAlert(error: errorState.error)
        case .focusOnTokenField:
            tokenTextField.becomeFirstResponder()
        }
    }

    private func showErrorAlert(error: ErrorModel) {
        let title = R.string.localizable.error()
        let message = error.title.localized()
        let postfix = error.message.localized()
        let fullMessage = "\(message) \n\(postfix)"

        showAlert?(AlertModel(title: title, message: fullMessage))
    }

    @IBAction private func signInButtonAction(_ _: UIButton) {
        viewModel.onSignButtonPressed()
    }

    deinit {
        stateTask?.cancel()
        actionTask?.cancel()
    }
}
