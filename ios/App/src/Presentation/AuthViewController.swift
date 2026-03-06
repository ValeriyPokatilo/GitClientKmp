import MultiPlatformLibrary
import RxKeyboard
import RxSwift
import UIKit

final class AuthViewController: UIViewController {

    @IBOutlet private weak var tokenTextField: UITextField!
    @IBOutlet private weak var errorLabel: UILabel!
    @IBOutlet private weak var signInButton: UIButton!
    @IBOutlet private weak var bottomConstraint: NSLayoutConstraint!

    private let disposeBag = DisposeBag()

    override func viewDidLoad() {
        super.viewDidLoad()
        setupUI()
        setupKeyboardBinding()
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
        tokenTextField.layer.borderColor = UIColor.grey.cgColor

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

    @IBAction private func signInButtonAction(_ sender: Any) {
        // TODO: - viewModel.onSignInButtonPressed
    }
}
