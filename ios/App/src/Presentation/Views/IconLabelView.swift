import UIKit

final class IconLabelView: UIView {

    @IBOutlet private weak var iconImageView: UIImageView!
    @IBOutlet private weak var titleLabel: UILabel!
    @IBOutlet private weak var additionalLabel: UILabel!

    var onTap: EmptyBlock?

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
        let nib = UINib(nibName: "IconLabelView", bundle: bundle)

        guard let view = nib.instantiate(withOwner: self).first as? UIView
        else {
            fatalError("IconLabelView nib not found")
        }

        view.frame = bounds
        view.autoresizingMask = [.flexibleWidth, .flexibleHeight]

        addSubview(view)

        setupTapGesture()
    }

    func configure(
        icon: UIImage?,
        title: String,
        titleColor: UIColor,
        additional: String?,
    ) {
        iconImageView.image = icon
        titleLabel.text = title
        titleLabel.textColor = titleColor
        additionalLabel.text = additional ?? ""
    }

    private func setupTapGesture() {
        let tap = UITapGestureRecognizer(
            target: self,
            action: #selector(handleTap)
        )
        addGestureRecognizer(tap)
    }

    @objc private func handleTap() {
        onTap?()
    }
}
