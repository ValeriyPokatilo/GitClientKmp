import UIKit
import MultiPlatformLibrary

final class AuthViewController: UIViewController {
    
    @IBOutlet private weak var tokenTextField: UITextField!
    @IBOutlet private weak var signInButton: UIButton!
    @IBOutlet private weak var errorLabel: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setupUI()
    }
    
    private func setupUI() {
        let placeholder = MR.strings().token_text_field_placeholder.desc().localized()
        tokenTextField.attributedPlaceholder = NSAttributedString(
            string: placeholder,
            attributes: [NSAttributedString.Key.foregroundColor: UIColor.white50]
        )
        tokenTextField.layer.borderColor = UIColor.grey.cgColor
        
        let paddingView = UIView(
            frame: CGRect(x: 0, y: 0, width: 16, height: tokenTextField.frame.height)
        )

        tokenTextField.leftView = paddingView
        tokenTextField.leftViewMode = .always
        
        signInButton.setTitle(
            MR.strings().sign_in_button_title.desc().localized(),
            for: .normal
        )
    }
    
    @IBAction private func signInButtonAction(_ sender: Any) {
        // TODO: - viewModel.onSignInButtonPressed
    }
}
