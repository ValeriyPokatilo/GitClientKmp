//
// Copyright (c) 2025 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
//

import Combine
import SwiftUI

@propertyWrapper
struct PropertyStateObject<V>: DynamicProperty {
    @StateObject private var wrappedObject: ObservableObjectWrapper<V>

    var wrappedValue: V {
        wrappedObject.value
    }

    init(wrappedValue: @autoclosure @escaping () -> V) {
        _wrappedObject = StateObject(wrappedValue: ObservableObjectWrapper(value: wrappedValue()))
    }
}

class ObservableObjectWrapper<V>: ObservableObject {
    let value: V

    init(value: V) {
        self.value = value
    }
}
