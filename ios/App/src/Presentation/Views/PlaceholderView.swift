import MultiPlatformLibrary
import UIKit

final class PlaceholderView: UIView {
    @IBOutlet private var imageView: UIImageView!
    @IBOutlet private var titleLabel: UILabel!
    @IBOutlet private var messageLabel: UILabel!
    @IBOutlet private var refreshButton: UIButton!

    private var action: EmptyBlock?

    override init(frame: CGRect) {
        super.init(frame: frame)
        loadFromNib()
    }

    required init?(coder: NSCoder) {
        super.init(coder: coder)
        loadFromNib()
    }

    private func loadFromNib() {
        let bundle = Bundle(for: Self.self)
        let nib = UINib(nibName: "PlaceholderView", bundle: bundle)

        guard let view = nib.instantiate(withOwner: self).first as? UIView
        else {
            fatalError("PlaceholderView nib not found")
        }

        view.frame = bounds
        view.autoresizingMask = [.flexibleWidth, .flexibleHeight]

        addSubview(view)
    }

    func configure(with error: ErrorModel, action: @escaping EmptyBlock) {
        reset()

        imageView.image = error.icon.toUIImage()
        titleLabel.text = error.title.localized()
        titleLabel.textColor = .appError
        messageLabel.text = error.message.localized()
        refreshButton.setTitle(
            R.string.localizable.retry(),
            for: .normal
        )

        self.action = action
    }

    func configureEmpty(action: @escaping EmptyBlock) {
        reset()

        imageView.image = R.image.ic_empty()
        titleLabel.text = R.string.localizable.repositories_empty_title()
        titleLabel.textColor = R.color.appBlue()!
        messageLabel.text = R.string.localizable.repositories_empty_message()
        refreshButton.setTitle(
            R.string.localizable.refresh(),
            for: .normal
        )

        self.action = action
    }

    @IBAction private func refreshAction(_ _: UIButton) {
        action?()
    }

    private func reset() {
        imageView.image = nil
        titleLabel.text = nil
        messageLabel.text = nil
        titleLabel.textColor = .label
        action = nil
    }
}
