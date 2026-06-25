import UIKit

extension UIAlertController {
    func setBackgroundColor(_ color: UIColor) {
        guard let backgroundView = view.subviews.first,
              let groupView = backgroundView.subviews.first,
              let contentView = groupView.subviews.first
        else { return }

        contentView.backgroundColor = color
    }

    func setTitleColor(_ color: UIColor) {
        guard let title else { return }

        let attributeString = NSMutableAttributedString(string: title)
        attributeString.addAttributes(
            [NSAttributedString.Key.foregroundColor: color],
            range: NSMakeRange(0, title.utf8.count)
        )

        setValue(attributeString, forKey: "attributedTitle")
    }

    func setMessageColor(_ color: UIColor) {
        guard let message else { return }

        let attributeString = NSMutableAttributedString(string: message)
        attributeString.addAttributes(
            [NSAttributedString.Key.foregroundColor: color],
            range: NSMakeRange(0, message.utf8.count)
        )

        setValue(attributeString, forKey: "attributedMessage")
    }

    func setTintColor(_ color: UIColor) {
        view.tintColor = color
    }
}
