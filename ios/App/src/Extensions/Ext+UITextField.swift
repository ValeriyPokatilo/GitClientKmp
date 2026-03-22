import UIKit

extension UITextField {
    func setupBorderedField(placeholder: String) {
        self.layer.borderColor = UIColor.appGrey.cgColor

        self.attributedPlaceholder = NSAttributedString(
            string: placeholder,
            attributes: [NSAttributedString.Key.foregroundColor: UIColor.white50]
        )

        let paddingView = UIView(
            frame: CGRect(
                x: 0,
                y: 0,
                width: 16,
                height: self.frame.height
            )
        )

        self.leftView = paddingView
        self.leftViewMode = .always
    }
}
