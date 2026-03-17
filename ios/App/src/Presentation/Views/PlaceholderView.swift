import MultiPlatformLibrary
import UIKit

final class PlaceholderView: UIView {

    @IBOutlet private weak var imageView: UIImageView!
    @IBOutlet private weak var titleLabel: UILabel!
    @IBOutlet private weak var messageLabel: UILabel!
    @IBOutlet private weak var refreshButton: UIButton!

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

    func configure(with error: AppError, action: @escaping EmptyBlock) {
        reset()
        
        let title: String
        let message: String
        let icon: UIImage?

        switch error {
        case let httpError as AppError.Http:
            title = "\(httpError.code)"
            message = httpError.message ?? ""
            icon = R.image.ic_error()

        case is AppError.Network:
            title = MR.strings().repositories_connection_error_title.desc()
                .localized()
            message = MR.strings().repositories_connection_error_message.desc()
                .localized()
            icon = R.image.ic_connection_error()

        default:
            title = ""
            message = error.message ?? ""
            icon = R.image.ic_error()
        }

        imageView.image = icon
        titleLabel.text = title
        titleLabel.textColor = .appError
        messageLabel.text = message
        refreshButton.setTitle(
            MR.strings().retry.desc().localized(),
            for: .normal
        )

        self.action = action
    }

    func configureEmpty(action: @escaping EmptyBlock) {
        reset()
        
        imageView.image = R.image.ic_empty()
        titleLabel.text = MR.strings().repositories_empty_title.desc()
            .localized()
        titleLabel.textColor = .appBlue
        messageLabel.text = MR.strings().repositories_empty_message.desc()
            .localized()
        refreshButton.setTitle(
            MR.strings().refresh.desc().localized(),
            for: .normal
        )

        self.action = action
    }

    @IBAction private func refreshAction(_ sender: Any) {
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
