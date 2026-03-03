//
// Copyright (c) 2025 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
//

import SwiftUI

private struct ViewSizeModifier: ViewModifier {
    let onCalculate: (CGSize) -> Void

    func body(content: Content) -> some View {
        content
            .background(
                GeometryReader { geometry in
                    Color.clear
                        .onAppear {
                            onCalculate(geometry.size)
                        }
                        .onChange(of: geometry.size) { newSize in
                            onCalculate(newSize)
                        }
                }
            )
    }
}

/// Модификатор для высчитывания размеров вью после рендера
///
/// - Parameter onCalculate: лямбда с получением CGSize после вычисления
extension View {
    func viewSize(onCalculate: @escaping (CGSize) -> Void) -> some View {
        self.modifier(ViewSizeModifier(onCalculate: onCalculate))
    }
}
