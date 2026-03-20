import MultiPlatformLibrary
import UIKit

extension ErrorIcon {
    func toUIImage() -> UIImage? {
        switch self {
        case is ErrorIcon.Network:
            UIImage.icConnectionError

        case is ErrorIcon.Http:
            UIImage.icError

        default:
            nil
        }
    }
}
