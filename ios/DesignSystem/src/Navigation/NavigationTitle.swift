//
// Copyright (c) 2025 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
//

import SwiftUI

public struct NavigationTitle: View {
    public enum TitleMode {
        case inline
        case large
    }

    @Environment(\.appTheme) var theme

    private let title: String
    private let mode: TitleMode

    public init(title: String, mode: TitleMode = .large) {
        self.title = title
        self.mode = mode
    }

    public var body: some View {
        Text(title)
            .font(titleFont)
            .foregroundStyle(theme.colors.onSurfaceColor)
            .lineLimit(2)
            .multilineTextAlignment(.leading)
            .frame(maxWidth: .infinity, alignment: .topLeading)
    }

    private var titleFont: Font {
        switch mode {
        case .inline:
            return theme.typography.bold(size: 20)
        case .large:
            return theme.typography.headerH1
        }
    }
}
