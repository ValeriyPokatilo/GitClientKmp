import UIKit

extension UIAlertController {

    func setBackgroundColor(_ color: UIColor) {
        guard let backgroundView = self.view.subviews.first,
            let groupView = backgroundView.subviews.first,
            let contentView = groupView.subviews.first
        else { return }
        
        contentView.backgroundColor = color
    }

    func setTitleColor(_ color: UIColor) {
        guard let title = self.title else { return }

        let attributeString = NSMutableAttributedString(string: title)
        attributeString.addAttributes(
            [NSAttributedString.Key.foregroundColor: color],
            range: NSMakeRange(0, title.utf8.count)
        )

        self.setValue(attributeString, forKey: "attributedTitle")
    }

    func setMessageColor(_ color: UIColor) {
        guard let message = self.message else { return }

        let attributeString = NSMutableAttributedString(string: message)
        attributeString.addAttributes(
            [NSAttributedString.Key.foregroundColor: color],
            range: NSMakeRange(0, message.utf8.count)
        )

        self.setValue(attributeString, forKey: "attributedMessage")
    }

    func setTintColor(_ color: UIColor) {
        self.view.tintColor = color
    }
}
