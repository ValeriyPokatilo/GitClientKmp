import MultiPlatformLibrary
import UIKit

extension ErrorIcon {
    func toUIImage() -> UIImage? {
        switch onEnum(of: self) {
        case .network:
            R.image.ic_connection_error()
        case .http:
            R.image.ic_error()
        }
    }
}
