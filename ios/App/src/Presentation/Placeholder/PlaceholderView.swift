import MultiPlatformLibrary
import UIKit

final class PlaceholderView: UIView {

    @IBOutlet private weak var imageView: UIImageView!
    @IBOutlet private weak var titleLabel: UILabel!
    @IBOutlet private weak var messageLabel: UILabel!
    @IBOutlet weak var refreshButton: UIButton!

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

    func configure(
        image: UIImage?,
        title: String,
        message: String,
        isError: Bool,
        action: @escaping EmptyBlock
    ) {
        imageView.image = image
        titleLabel.text = title
        titleLabel.textColor = isError ? R.color.appError() : R.color.appBlue()
        messageLabel.text = message
        refreshButton.setTitle(
            isError
                ? MR.strings().retry.desc().localized()
                : MR.strings().refresh.desc().localized(),
            for: .normal
        )
        self.action = action
    }

    @IBAction func refrashAction(_ sender: Any) {
        action?()
    }
}
