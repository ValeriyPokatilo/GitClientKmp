import PhotosUI
import UIKit

final class ImagePicker: NSObject {
    private weak var viewController: UIViewController?
    private var completion: (([Data]) -> Void)?

    init(viewController: UIViewController) {
        self.viewController = viewController
    }

    func present(completion: @escaping ([Data]) -> Void) {
        self.completion = completion

        var config = PHPickerConfiguration()
        config.filter = .images
        config.selectionLimit = 0

        let picker = PHPickerViewController(configuration: config)
        picker.delegate = self

        viewController?.present(picker, animated: true)
    }
}

extension ImagePicker: PHPickerViewControllerDelegate {
    func picker(
        _ picker: PHPickerViewController,
        didFinishPicking results: [PHPickerResult]
    ) {
        picker.dismiss(animated: true)

        guard !results.isEmpty else {
            completion?([])
            return
        }

        let group = DispatchGroup()
        var dataArray: [Data] = []

        for result in results {
            let provider = result.itemProvider

            if provider.hasItemConformingToTypeIdentifier(UTType.image.identifier) {
                group.enter()

                provider.loadDataRepresentation(
                    forTypeIdentifier: UTType.image.identifier
                ) { data, _ in
                    if let data {
                        dataArray.append(data)
                    }
                    group.leave()
                }
            }
        }

        group.notify(queue: .main) { [weak self] in
            self?.completion?(dataArray)
        }
    }
}
