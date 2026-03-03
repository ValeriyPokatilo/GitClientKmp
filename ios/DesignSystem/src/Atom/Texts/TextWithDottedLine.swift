//
// Copyright (c) 2025 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
//

import SwiftUI

public struct TextWithDottedLine: View {
    @Environment(\.appTheme) private var theme

    private let text: String
    private let details: String

    public init(_ text: String, details: String) {
        self.text = text
        self.details = details
    }

    public var body: some View {
        HStack(spacing: 10) {
            Text(text)
            Spacer()
                .overlay {
                    LineShape()
                        .stroke(
                            style: StrokeStyle(
                                dash: [7, 2]
                            )
                        )
                        .foregroundStyle(theme.colors.primaryColor)
                        .frame(height: 1)
                }
            Text(details)
        }
        .font(theme.typography.regular(size: 16))
        .foregroundStyle(theme.colors.onSurface)
        .frame(height: 22)
    }
}
