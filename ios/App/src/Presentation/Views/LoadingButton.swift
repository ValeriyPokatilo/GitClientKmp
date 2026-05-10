import NVActivityIndicatorView
import UIKit

final class LoadingButton: UIButton {
    enum Style {
        case primary
        case secondary
    }

    private let indicatorView: NVActivityIndicatorView = {
        let indicator = NVActivityIndicatorView(frame: .zero)
        indicator.type = .circleStrokeSpin
        indicator.stopAnimating()
        return indicator
    }()

    private let indicatorSize: CGFloat = 24

    override init(frame: CGRect) {
        super.init(frame: frame)
        commonInit()
    }

    required init?(coder: NSCoder) {
        super.init(coder: coder)
        commonInit()
    }

    func configure(style: Style) {
        switch style {
        case .primary:
            setTitleColor(.white, for: .normal)
            backgroundColor = R.color.appGreen()
        case .secondary:
            setTitleColor(R.color.appLightGreen(), for: .normal)
            backgroundColor = .clear
            layer.borderColor = R.color.white50()?.cgColor
            layer.borderWidth = 1
        }

        titleLabel?.font = .systemFont(ofSize: 16, weight: .semibold)
        layer.cornerRadius = 8
        clipsToBounds = true
    }

    func startAnimating() {
        indicatorView.startAnimating()
        titleLabel?.alpha = 0
    }

    func stopAnimating() {
        indicatorView.stopAnimating()
        titleLabel?.alpha = 1
    }

    private func commonInit() {
        addSubview(indicatorView)

        indicatorView.translatesAutoresizingMaskIntoConstraints = false
        NSLayoutConstraint.activate([
            indicatorView.centerXAnchor.constraint(equalTo: centerXAnchor),
            indicatorView.centerYAnchor.constraint(equalTo: centerYAnchor),
            indicatorView.widthAnchor.constraint(equalToConstant: indicatorSize),
            indicatorView.heightAnchor.constraint(equalToConstant: indicatorSize),
        ])
    }
}
