import MultiPlatformLibrary
import UIKit

final class CreateIssueCoordinator {
    private let navigationController: UINavigationController
    private let owner: String
    private let repositoryName: String

    private var imagePicker: ImagePicker?

    init(
        navigationController: UINavigationController,
        owner: String,
        repositoryName: String
    ) {
        self.navigationController = navigationController
        self.owner = owner
        self.repositoryName = repositoryName
    }

    func start() {
        let controller = CreateIssueViewController(
            owner: owner,
            repositoryName: repositoryName
        )

        controller.showAlert = { [weak self] alertModel in
            self?.showErrorAlert(
                title: alertModel.title,
                message: alertModel.message
            )
        }

        controller.popViewController = { [weak self] in
            self?.navigationController.popViewController(animated: true)
        }

        controller.openImagePicker = { [weak self] in
            self?.showImagePicker(parent: controller)
        }

        navigationController.pushViewController(controller, animated: true)
    }

    private func showErrorAlert(title: String, message: String) {
        let alert = AlertViewController(title: title, message: message)
        navigationController.present(alert, animated: true)
    }

    private func showImagePicker(parent: CreateIssueViewController) {
        let picker = ImagePicker(viewController: parent)
        self.imagePicker = picker

        picker.present { [weak parent] dataArray in
            guard let parent else { return }

            let byteArrays = dataArray.map { $0.toKotlinByteArray() }

            DispatchQueue.main.async {
                parent.onFilesSelected(byteArrays)
            }
        }
    }
}
