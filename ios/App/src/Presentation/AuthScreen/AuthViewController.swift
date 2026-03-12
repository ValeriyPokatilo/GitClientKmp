import MultiPlatformLibrary
import RxKeyboard
import RxSwift
import UIKit
import NVActivityIndicatorView

final class AuthViewController: UIViewController {

    @IBOutlet private weak var tokenTextField: UITextField!
    @IBOutlet private weak var errorLabel: UILabel!
    @IBOutlet private weak var signInButton: UIButton!
    @IBOutlet private weak var bottomConstraint: NSLayoutConstraint!
    
    private lazy var viewModel: AuthViewModel = Koin.instance.getAuthViewModel()
    
    private let indicatorView = NVActivityIndicatorView(
        frame: .zero,
        type: .circleStrokeSpin,
        color: .white,
        padding: 0
    )
    private let indicatorViewSize: CGFloat = 24

    private let disposeBag = DisposeBag()
    
    var routeToMain: EmptyBlock?
    var showAlert: ParameterBlock<(String, String)>?

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
        
        signInButton.addSubview(indicatorView)
        indicatorView.translatesAutoresizingMaskIntoConstraints = false
        NSLayoutConstraint.activate([
            indicatorView.centerXAnchor.constraint(
                equalTo: signInButton.centerXAnchor
            ),
            indicatorView.centerYAnchor.constraint(
                equalTo: signInButton.centerYAnchor
            ),
            indicatorView.widthAnchor.constraint(
                equalToConstant: indicatorViewSize
            ),
            indicatorView.heightAnchor.constraint(
                equalToConstant: indicatorViewSize
            )
        ])
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
            signInButton.titleLabel?.isHidden = false
            tokenTextField.layer.borderColor = UIColor.appGrey.cgColor
            indicatorView.stopAnimating()

        case is AuthViewModelStateLoading:
            signInButton.isEnabled = false
            signInButton.titleLabel?.isHidden = true
            indicatorView.startAnimating()

        case is AuthViewModelStateInvalidInput:
            errorLabel.isHidden = false
            errorLabel.text = MR.strings().invalid_token_reason.desc()
                .localized()
            tokenTextField.layer.borderColor = UIColor.red.cgColor
            signInButton.isEnabled = false
            indicatorView.stopAnimating()

        default: break
        }
    }

    private func handleAction(_ action: AuthViewModelAction) {
        if action is AuthViewModelActionRouteToMain {
            routeToMain?()
        } else if let errorAction = action as? AuthViewModelActionShowError {
            showErrorAlert(message: errorAction.message)
        } else if action is AuthViewModelActionFocusOnTokenField {
            tokenTextField.becomeFirstResponder()
        }
    }
    
    private func showErrorAlert(message: String?) {
        let title = MR.strings().error.desc().localized()
        let baseMessage = message ?? MR.strings().check_connection.desc().localized()
        let messagePostfix = MR.strings().info_for_developer.desc().localized()
        let fullMessage = "\(baseMessage)\n\(messagePostfix)"
        
        showAlert?((title, fullMessage))
    }

    @IBAction private func signInButtonAction(_ sender: Any) {
        viewModel.onSignButtonPressed()
    }
}
