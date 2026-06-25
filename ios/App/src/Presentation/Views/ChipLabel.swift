import UIKit

final class ChipLabel: UILabel {
    private let horizontalInset: CGFloat = 12
    private let verticalInset: CGFloat = 4

    override func drawText(in rect: CGRect) {
        let insets = UIEdgeInsets(
            top: verticalInset,
            left: horizontalInset,
            bottom: verticalInset,
            right: horizontalInset
        )
        super.drawText(in: rect.inset(by: insets))
    }

    override var intrinsicContentSize: CGSize {
        let size = super.intrinsicContentSize
        return CGSize(
            width: size.width + horizontalInset * 2,
            height: size.height + verticalInset * 2
        )
    }

    func configure(
        text: String,
        textColor: UIColor,
        backgroundColor: UIColor
    ) {
        self.text = text
        self.textColor = textColor
        self.backgroundColor = backgroundColor
        self.layer.cornerRadius = 8
        self.layer.masksToBounds = true
        self.textAlignment = .center
    }
}
