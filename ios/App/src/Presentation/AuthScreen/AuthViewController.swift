import MultiPlatformLibrary
import NVActivityIndicatorView
import RxKeyboard
import RxSwift
import UIKit

final class AuthViewController: UIViewController {

    @IBOutlet private weak var tokenTextField: UITextField!
    @IBOutlet private weak var errorLabel: UILabel!
    @IBOutlet private weak var signInButton: UIButton!
    @IBOutlet private weak var bottomConstraint: NSLayoutConstraint!
    @IBOutlet private weak var indicatorView: NVActivityIndicatorView!

    private lazy var viewModel: AuthViewModel = Koin.instance.getAuthViewModel()

    private let disposeBag = DisposeBag()
    
    private var stateTask: Task<Void, Never>?
    private var actionTask: Task<Void, Never>?

    var routeToMain: EmptyBlock?
    var showAlert: ParameterBlock<AlertModel>?

    override func viewDidLoad() {
        super.viewDidLoad()
        setupUI()
        bindKeyboard()
        bindViewModel()
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        navigationController?.setNavigationBarHidden(true, animated: animated)
    }

    private func setupUI() {
        let placeholder = MR.strings().token_text_field_placeholder.desc()
            .localized()
        tokenTextField.setupBorderedField(placeholder: placeholder)
        
        
        signInButton.setTitle(
            MR.strings().sign_in_button_title.desc().localized(),
            for: .normal
        )

        indicatorView.type = .circleStrokeSpin
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
        switch state {
        case is AuthViewModelStateIdle:
            handleIdleState()

        case is AuthViewModelStateLoading:
            handleLoadingState()

        case is AuthViewModelStateInvalidInput:
            handleInvalidInputState()

        default: break
        }
    }
    
    private func handleIdleState() {
        errorLabel.isHidden = true
        signInButton.isEnabled = true
        signInButton.titleLabel?.isHidden = false
        tokenTextField.layer.borderColor = UIColor.appGrey.cgColor
        indicatorView.stopAnimating()
    }
    
    private func handleLoadingState() {
        errorLabel.isHidden = true
        signInButton.isEnabled = false
        signInButton.titleLabel?.isHidden = true
        indicatorView.startAnimating()
    }
    
    private func handleInvalidInputState() {
        errorLabel.isHidden = false
        errorLabel.text = MR.strings().invalid_token_reason.desc()
            .localized()
        tokenTextField.layer.borderColor = UIColor.red.cgColor
        signInButton.isEnabled = false
        indicatorView.stopAnimating()
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
        let title = MR.strings().error.desc().localized()
        let code = error.title.localized()
        let message = error.message.localized()
        let fullMessage = "\(code) / \(message)"

        showAlert?(AlertModel(title: title, message: fullMessage))
    }

    @IBAction private func signInButtonAction(_ sender: Any) {
        viewModel.onSignButtonPressed()
    }
    
    deinit {
        stateTask?.cancel()
        actionTask?.cancel()
    }
}
