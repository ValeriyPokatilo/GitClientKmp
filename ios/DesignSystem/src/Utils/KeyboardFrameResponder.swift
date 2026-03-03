//
// Copyright (c) 2025 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
//

import Combine
import SwiftUI

class KeyboardFrameResponder: ObservableObject {
    @Published var currentFrame: CGRect? = nil

    private var cancellable: AnyCancellable?

    init() {
        cancellable = Publishers.Merge(
            NotificationCenter.default.publisher(for: UIResponder.keyboardWillShowNotification),
            NotificationCenter.default.publisher(for: UIResponder.keyboardWillChangeFrameNotification)
        )
        .compactMap { $0.userInfo?[UIResponder.keyboardFrameEndUserInfoKey] as? CGRect }
        .merge(
            with: NotificationCenter.default
                .publisher(for: UIResponder.keyboardWillHideNotification)
                .map { _ in nil }
        )
        .assign(to: \.currentFrame, on: self)
    }

    deinit {
        cancellable?.cancel()
    }
}
