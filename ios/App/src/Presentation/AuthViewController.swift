import MultiPlatformLibrary
import RxKeyboard
import RxSwift
import UIKit

final class AuthViewController: UIViewController {

    @IBOutlet private weak var tokenTextField: UITextField!
    @IBOutlet private weak var errorLabel: UILabel!
    @IBOutlet private weak var signInButton: UIButton!
    @IBOutlet private weak var bottomConstraint: NSLayoutConstraint!
    
    var routeToMain: EmptyBlock?
    var showAlert: ParameterBlock<String>?

    private let disposeBag = DisposeBag()

    private lazy var viewModel: AuthViewModel = Koin.instance.getAuthViewModel()

    override func viewDidLoad() {
        super.viewDidLoad()
        setupUI()
        setupKeyboardBinding()
        bindViewModel()
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        navigationController?.setNavigationBarHidden(true, animated: animated)
    }

    private func setupUI() {
        let placeholder = MR.strings().token_text_field_placeholder.desc()
            .localized()
        tokenTextField.attributedPlaceholder = NSAttributedString(
            string: placeholder,
            attributes: [
                NSAttributedString.Key.foregroundColor: UIColor.white50
            ]
        )
        tokenTextField.layer.borderColor = UIColor.appGrey.cgColor

        let paddingView = UIView(
            frame: CGRect(
                x: 0,
                y: 0,
                width: 16,
                height: tokenTextField.frame.height
            )
        )

        tokenTextField.leftView = paddingView
        tokenTextField.leftViewMode = .always

        signInButton.setTitle(
            MR.strings().sign_in_button_title.desc().localized(),
            for: .normal
        )
    }

    private func setupKeyboardBinding() {
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

        Task { [weak self] in
            guard let self = self else { return }
            for await state in self.viewModel.state {
                self.renderState(state)
            }
        }

        Task { [weak self] in
            guard let self = self else { return }
            for await action in self.viewModel.action {
                self.handleAction(action)
            }
        }
    }

    private func renderState(_ state: AuthViewModelState) {
        switch state {
        case is AuthViewModelStateIdle:
            errorLabel.isHidden = true
            signInButton.isEnabled = true
            signInButton.alpha = 1.0
            tokenTextField.layer.borderColor = UIColor.appGrey.cgColor
        // TODO: - hide indicator

        case is AuthViewModelStateLoading:
            tokenTextField.resignFirstResponder()
            signInButton.isEnabled = false
            signInButton.alpha = 0.5
        // TODO: - show indicator

        case is AuthViewModelStateInvalidInput:
            errorLabel.isHidden = false
            errorLabel.text = MR.strings().invalid_token_reason.desc()
                .localized()
            tokenTextField.layer.borderColor = UIColor.red.cgColor
            signInButton.isEnabled = false

        default: break
        }
    }

    private func handleAction(_ action: AuthViewModelAction) {
        if action is AuthViewModelActionRouteToMain {
            routeToMain?()
        } else if let errorAction = action as? AuthViewModelActionShowError {
            showAlert?(errorAction.message ?? "")
        } else if action is AuthViewModelActionFocusOnTokenField {
            tokenTextField.becomeFirstResponder()
        }
    }

    @IBAction private func signInButtonAction(_ sender: Any) {
        viewModel.onSignButtonPressed()
    }
}
