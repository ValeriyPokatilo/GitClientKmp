import CoreImage
import UIKit
import MultiPlatformLibrary

final class AlertViewController: UIViewController {

    private let alertTitle: String
    private let alertMessage: String

    init(title: String, message: String) {
        self.alertTitle = title
        self.alertMessage = message
        super.init(nibName: nil, bundle: nil)

        modalPresentationStyle = .overFullScreen
        modalTransitionStyle = .crossDissolve
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .clear
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        setupBlurFilter()
        showAlert()
    }

    private func setupBlurFilter() {
        guard
            let window = view.window,
            let rootView = window.rootViewController?.view
        else { return }

        let renderer = UIGraphicsImageRenderer(size: rootView.bounds.size)
        let screenshot = renderer.image { _ in
            rootView.drawHierarchy(
                in: rootView.bounds,
                afterScreenUpdates: true
            )
        }

        guard let ciImage = CIImage(image: screenshot) else { return }

        let blurFilter = CIFilter(name: "CIGaussianBlur")
        blurFilter?.setValue(ciImage, forKey: kCIInputImageKey)
        blurFilter?.setValue(6, forKey: kCIInputRadiusKey)

        guard let blurredOutput = blurFilter?.outputImage else { return }

        let context = CIContext()

        guard let cgImage = context.createCGImage(
                blurredOutput,
                from: ciImage.extent
            )
        else { return }

        let blurredImage = UIImage(cgImage: cgImage)

        let imageView = UIImageView(image: blurredImage)
        imageView.frame = view.bounds
        imageView.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        imageView.contentMode = .scaleToFill
        view.addSubview(imageView)

        let overlayView = UIView(frame: view.bounds)
        overlayView.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        overlayView.backgroundColor = UIColor.white.withAlphaComponent(0.1)

        view.addSubview(overlayView)
    }

    private func showAlert() {
        let alertController = UIAlertController(
            title: alertTitle,
            message: alertMessage,
            preferredStyle: .alert
        )

        alertController.setBackgroundColor(.appBackground)
        alertController.setTitleColor(.white)
        alertController.setMessageColor(.white)
        alertController.setTintColor(.appBlue)

        let okAction = UIAlertAction(
            title: MR.strings().ok.desc().localized(),
            style: .default
        ) { [weak self] _ in
            self?.dismiss(animated: true)
        }

        alertController.addAction(okAction)
        present(alertController, animated: true)
    }
}
