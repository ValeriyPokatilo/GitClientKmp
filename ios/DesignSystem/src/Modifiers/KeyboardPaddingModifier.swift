//
// Copyright (c) 2025 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
//

import SwiftUI

private struct KeyboardPaddingModifier: ViewModifier {
    @StateObject private var keyboardResponder = KeyboardFrameResponder()

    func body(content: Content) -> some View {
        return GeometryReader { geometry in
            content
                .padding(
                    .bottom,
                    keyboardOverlayHeight(
                        viewFrame: geometry.frame(in: .global),
                        keyboardFrame: keyboardResponder.currentFrame
                    )
                )
        }
    }

    private func keyboardOverlayHeight(
        viewFrame: CGRect,
        keyboardFrame: CGRect?
    ) -> CGFloat {
        guard let keyboardFrame else { return 0 }

        return max(viewFrame.maxY - keyboardFrame.minY, 0)
    }
}

public extension View {
    /// Добавляет отступ снизу на размер, который перекрывает клавиатура поверх текущей View
    func keyboardPadding() -> some View {
        modifier(KeyboardPaddingModifier())
    }
}
