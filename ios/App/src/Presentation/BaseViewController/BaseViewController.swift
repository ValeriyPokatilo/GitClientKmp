import MultiPlatformLibrary
import NVActivityIndicatorView
import UIKit

class BaseViewController: UIViewController {
    private var placeholderView: PlaceholderView?
    private var indicatorView: NVActivityIndicatorView?

    var showAlert: ParameterBlock<AlertModel>?

    func showEmptyRepositories() {
        addPlaceholderIfNeeded()
        placeholderView?.configureEmptyRepositories()
        placeholderView?.isHidden = false
    }

    func showEmptyIssues() {
        addPlaceholderIfNeeded()
        placeholderView?.configureEmptyIssues()
        placeholderView?.isHidden = false
    }

    func showErrorPlaceholder(error: ErrorModel) {
        addPlaceholderIfNeeded()
        placeholderView?.configure(with: error)
        placeholderView?.isHidden = false
    }

    func hidePlaceholder() {
        placeholderView?.removeFromSuperview()
        placeholderView = nil
    }

    func startLoading() {
        addIndicatorIfNeeded()
        indicatorView?.startAnimating()
        indicatorView?.isHidden = false
    }

    func stopLoading() {
        indicatorView?.stopAnimating()
        indicatorView?.removeFromSuperview()
        indicatorView = nil
    }

    func showErrorAlert(error: ErrorModel) {
        showAlert?(AlertModel(
            title: R.string.localizable.error(),
            message: error.alertMessage?.localized() ?? ""
        ))
    }

    private func addIndicatorIfNeeded() {
        guard indicatorView == nil else { return }

        let indicator = NVActivityIndicatorView(frame: .zero)
        indicator.type = .circleStrokeSpin
        indicator.color = .white
        indicator.translatesAutoresizingMaskIntoConstraints = false

        view.addSubview(indicator)

        NSLayoutConstraint.activate([
            indicator.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            indicator.centerYAnchor.constraint(equalTo: view.centerYAnchor),
            indicator.widthAnchor.constraint(equalToConstant: 56),
            indicator.heightAnchor.constraint(equalToConstant: 56),
        ])

        indicatorView = indicator
    }

    private func addPlaceholderIfNeeded() {
        guard placeholderView == nil else { return }

        let placeholder = PlaceholderView()
        placeholder.translatesAutoresizingMaskIntoConstraints = false

        view.insertSubview(placeholder, at: 0)

        NSLayoutConstraint.activate([
            placeholder.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            placeholder.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            placeholder.bottomAnchor.constraint(
                equalTo: view.safeAreaLayoutGuide.bottomAnchor
            ),
            placeholder.topAnchor.constraint(equalTo: view.topAnchor),
        ])

        placeholderView = placeholder
    }
}
