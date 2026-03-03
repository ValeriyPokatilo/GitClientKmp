//
// Copyright (c) 2025 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
//

import SwiftUI

public struct ViewOffsetKey: PreferenceKey {
    public static var defaultValue: CGFloat = 0
    public static func reduce(value: inout CGFloat, nextValue: () -> CGFloat) {
        value = nextValue()
    }
}

private struct ScrollOffsetModifier: ViewModifier {
    var onChange: ((CGFloat) -> Void)?

    func body(content: Content) -> some View {
        content
            .overlay {
                GeometryReader { geo in
                    let minY = geo.frame(in: .global).minY
                    Color.clear
                        .preference(key: ViewOffsetKey.self, value: minY)
                        .onPreferenceChange(ViewOffsetKey.self) { offset in
                            DispatchQueue.main.async {
                                onChange?(offset)
                            }
                        }
                }
            }
    }
}

public extension View {
    // Модификатор для определения офсета скролл вью
    @ViewBuilder
    func offsetY(
        completion: @escaping ((CGFloat) -> Void)
    ) -> some View {
        self.modifier(ScrollOffsetModifier(onChange: completion))
    }
}
