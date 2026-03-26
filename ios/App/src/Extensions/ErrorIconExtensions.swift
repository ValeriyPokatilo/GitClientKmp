import MultiPlatformLibrary
import UIKit

extension ErrorIcon {
    func toUIImage() -> UIImage? {
        switch self {
        case is ErrorIcon.Network:
            R.image.ic_connection_error()

        case is ErrorIcon.Http:
            R.image.ic_error()

        default:
            nil
        }
    }
}
