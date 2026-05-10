import MultiPlatformLibrary
import UIKit

final class PlaceholderView: UIView {
    @IBOutlet private var imageView: UIImageView!
    @IBOutlet private var titleLabel: UILabel!
    @IBOutlet private var messageLabel: UILabel!

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

    func configure(with error: ErrorModel) {
        reset()

        if error.isNetworkError {
            imageView.image = R.image.ic_connection_error()
        } else {
            imageView.image = R.image.ic_error()
        }

        titleLabel.text = error.placeholderTitle.localized()
        titleLabel.textColor = .appError
        messageLabel.text = error.placeholderMessage.localized()
    }

    func configureEmptyRepositories() {
        configureEmptyPlaceholder(
            title: R.string.localizable.repositories_empty_title(),
            message: R.string.localizable.repositories_empty_message()
        )
    }

    func configureEmptyIssues() {
        configureEmptyPlaceholder(
            title: R.string.localizable.issues_empty_title(),
            message: R.string.localizable.issues_empty_message()
        )
    }

    private func configureEmptyPlaceholder(
        title: String,
        message: String
    ) {
        reset()

        imageView.image = R.image.ic_empty()
        titleLabel.text = title
        titleLabel.textColor = R.color.appBlue()!
        messageLabel.text = message
    }

    private func reset() {
        imageView.image = nil
        titleLabel.text = nil
        messageLabel.text = nil
        titleLabel.textColor = .label
    }
}
