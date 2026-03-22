import MultiPlatformLibrary
import UIKit

extension Optional where Wrapped == KotlinInt {
    var uiColor: UIColor {
        guard let value = self else { return UIColor.white }
        let intValue = Int(truncating: value)
        let a = CGFloat((intValue >> 24) & 0xFF) / 255.0
        let r = CGFloat((intValue >> 16) & 0xFF) / 255.0
        let g = CGFloat((intValue >> 8) & 0xFF) / 255.0
        let b = CGFloat(intValue & 0xFF) / 255.0
        return UIColor(red: r, green: g, blue: b, alpha: a)
    }
}
