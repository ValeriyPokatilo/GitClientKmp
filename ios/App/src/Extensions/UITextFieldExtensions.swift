import UIKit

extension UITextField {
    func setupBorderedField(placeholder: String) {
        layer.borderColor = UIColor.appGrey.cgColor

        attributedPlaceholder = NSAttributedString(
            string: placeholder,
            attributes: [
                NSAttributedString.Key.foregroundColor: R.color.white50()!,
            ]
        )

        let paddingView = UIView(
            frame: CGRect(
                x: 0,
                y: 0,
                width: 16,
                height: frame.height
            )
        )

        leftView = paddingView
        leftViewMode = .always
        
        rightView = paddingView
        rightViewMode = .always
    }
}
