import UIKit

extension UITextView {
    private enum Constants {
        static let tag = 999
    }

    func setupBorderedField(placeholder: String) {
        layer.borderColor = UIColor.appGrey.cgColor
        layer.borderWidth = 1
        layer.cornerRadius = 8
        layer.masksToBounds = true

        textContainerInset = UIEdgeInsets(
            top: 12,
            left: 16,
            bottom: 12,
            right: 16
        )

        addPlaceholder(placeholder)
    }

    private func addPlaceholder(_ text: String) {
        let placeholderLabel = UILabel()
        placeholderLabel.text = text
        placeholderLabel.textColor = R.color.white50()
        placeholderLabel.font = self.font
        placeholderLabel.numberOfLines = 0
        placeholderLabel.tag = Constants.tag

        addSubview(placeholderLabel)

        placeholderLabel.translatesAutoresizingMaskIntoConstraints = false
        NSLayoutConstraint.activate([
            placeholderLabel.topAnchor.constraint(
                equalTo: topAnchor,
                constant: textContainerInset.top
            ),
            placeholderLabel.leadingAnchor.constraint(
                equalTo: leadingAnchor,
                constant: textContainerInset.left + 4
            ),
            placeholderLabel.trailingAnchor.constraint(
                lessThanOrEqualTo: trailingAnchor,
                constant: -textContainerInset.right
            ),
        ])

        NotificationCenter.default.addObserver(
            self,
            selector: #selector(textDidChange),
            name: UITextView.textDidChangeNotification,
            object: self
        )
    }

    @objc private func textDidChange() {
        viewWithTag(Constants.tag)?.isHidden = !text.isEmpty
    }
}
