import Foundation

public protocol SwiftEnumConvertible {
    associatedtype SwiftEnumType

    /// Метод для преобразования объекта Objective-C в соответствующий Swift-енум.
    func toSwiftEnum() -> SwiftEnumType
}
