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
        if state as? AuthViewModelStateInvalidInput != nil {
            errorLabel.text = MR.strings().invalid_token_reason.desc()
                .localized()
        }

        signInButton.isEnabled = !isLoading && !isInvalid
        signInButton.titleLabel?.isHidden = isLoading

        tokenTextField.layer.borderColor =
            (isInvalid ? UIColor.red : R.color.appGrey()!).cgColor

        if isLoading {
            indicatorView.startAnimating()
        } else {
            indicatorView.stopAnimating()
        }
    }

    private func handleAction(_ action: AuthViewModelAction) {
        if action is AuthViewModelActionRouteToMain {
            routeToMain?()
        } else if let errorAction = action as? AuthViewModelActionShowError {
            showErrorAlert(error: errorAction.error)
        } else if action is AuthViewModelActionFocusOnTokenField {
            tokenTextField.becomeFirstResponder()
        }
    }

    private func showErrorAlert(error: ErrorModel) {
        let title = R.string.localizable.error()
        let code = error.title.localized()
        let message = error.message.localized()
        let fullMessage = "\(code) / \(message)"

        showAlert?(AlertModel(title: title, message: fullMessage))
    }

    @IBAction private func signInButtonAction(_ sender: UIButton) {
        viewModel.onSignButtonPressed()
    }

    deinit {
        stateTask?.cancel()
        actionTask?.cancel()
    }
}
