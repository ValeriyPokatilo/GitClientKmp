//
// Copyright (c) 2025 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
//

import Combine
import MultiPlatformLibrary
import SwiftUI

// Проперти враппер для вьюмоделей, нужен для вызова onCleared после очищения экрана из памяти
// В обычной вьюшке не понятно, когда вызывать onCleared, тк View это структуры, которые не имеют deinit
@propertyWrapper
struct ViewModelWrapper<VM: ViewModel & ObservableObject>: DynamicProperty {
    // наш враппер сам является структурой - это нужно чтобы использовать
    // @StateObject, который корректно подпишется на изменения ObservableObject
    // но в тоже время нам нужен объект, у которого будет deinit для очистки
    // ViewModel. Поэтому наш враппер создает другой враппер - но уже объект.
    @StateObject var objectWrapper: VMWrapper<VM>

    var wrappedValue: VM { objectWrapper.viewModel }

    init(wrappedValue: @autoclosure @escaping () -> VM) {
        _objectWrapper = .init(
            wrappedValue: .init(
                wrappedValue: wrappedValue
            )
        )
    }
}

/// Объект-обертка над ViewModel, при уничтожении которого у ViewModel будет вызван onCleared
/// для остановки всех запущенных корутин и любой другой логики.
///
/// Также данная обертка подписывается на objectWillChange самой ViewModel'и и транслирует
/// уведомления об изменении своим слушателям, чтобы SwiftUI узнал когда нужно обновлять View.
class VMWrapper<VM: ViewModel & ObservableObject>: ObservableObject {
    fileprivate let viewModel: VM
    private var cancellation: AnyCancellable?

    init(wrappedValue: @escaping () -> VM) {
        // сначала нужно проинициализировать все свойства
        viewModel = wrappedValue()
        cancellation = nil

        // потом уже можно создать подписку использующую внутри closure
        // objectWillChange
        cancellation = viewModel.objectWillChange.sink { [weak objectWillChange] _ in
            objectWillChange?.send()
        }
    }

    deinit {
        cancellation?.cancel()
        viewModel.onCleared()
    }
}
