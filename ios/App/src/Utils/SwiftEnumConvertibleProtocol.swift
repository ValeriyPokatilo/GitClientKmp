//
// Copyright (c) 2025 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
//

import Foundation

public protocol SwiftEnumConvertible {
    associatedtype SwiftEnumType

    /// Метод для преобразования объекта Objective-C в соответствующий Swift-енум.
    func toSwiftEnum() -> SwiftEnumType
}
