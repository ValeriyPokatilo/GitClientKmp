//
// Copyright (c) 2025 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
//

import SwiftUI

private struct DismissesKeyboard: ViewModifier {
    func body(content: Content) -> some View {
        content
            .onTapGesture {
                dismissOtherKeyboards()
            }
    }
}

public func dismissOtherKeyboards() {
    UIApplication.shared.sendAction(
        #selector(
            UIResponder.resignFirstResponder
        ), to: nil, from: nil, for: nil
    )
}

public extension View {
    /// Модификатор для скрытия клавиатуры по тапу вне филдов,
    /// В отличие от простого .onTapGesture учитывает и пустоту вокруг контента
    func dismissesKeyboard() -> some View {
        modifier(DismissesKeyboard())
    }
}
