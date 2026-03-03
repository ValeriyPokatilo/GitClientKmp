//
// Copyright (c) 2025 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
//

import SwiftUI

public struct TextWithLineLabel<ButtonContent: View>: View {
    @Environment(\.appTheme) private var theme
    private let title: String?
    private let titleColor: Color?
    private let description: String?
    private let button: ButtonContent?
    private let onButtonClick: () -> Void

    @State private var titleHeight: CGFloat = .zero

    public init(
        title: String? = nil,
        titleColor: Color? = nil,
        description: String? = nil,
        @ViewBuilder button: () -> ButtonContent? = { EmptyView() },
        onButtonClick: (() -> Void)? = nil
    ) {
        self.title = title
        self.titleColor = titleColor
        self.description = description
        self.button = button()
        self.onButtonClick = onButtonClick ?? {}
    }

    private var titleForeground: Color {
        titleColor ?? theme.colors.onSurface
    }

    public var body: some View {
        HStack(spacing: 8.0) {
            RoundedRectangle(cornerRadius: 4.8)
                .fill(titleForeground)
                .frame(width: 4, height: titleHeight)

            VStack(alignment: .leading, spacing: 4) {
                if let title = title {
                    Text(title)
                        .foregroundStyle(titleForeground)
                        .font(theme.typography.button)
                }
                if let description = description {
                    Text(description)
                        .foregroundStyle(theme.colors.onSurface)
                        .font(theme.typography.button)
                }
            }
            .viewSize { size in
                titleHeight = size.height + 8
            }

            Spacer()

            if let button = button {
                Button(action: onButtonClick) {
                    button
                }
            }
        }
        .frame(maxWidth: .infinity, alignment: .leading)
    }
}

// MARK: - Preview

#Preview("TextWithLineLabel") {
    AppThemeProvider {
        VStack(spacing: 16) {
            // Пример с изображением
            TextWithLineLabel(
                title: "Title",
                description: "descriptive",
                button: {
                    Image(systemName: "pencil")
                        .resizable()
                        .frame(width: 24, height: 24)
                },
                onButtonClick: { print("Edit tapped") }
            )

            // Пример с текстом
            TextWithLineLabel(
                title: "Title без description",
                button: {
                    Text("Edit")
                        .foregroundColor(.blue)
                        .font(.system(size: 16, weight: .medium))
                },
                onButtonClick: { print("Edit tapped") }
            )
        }
        .padding()
        .background(Color.gray.opacity(0.1))
    }
}
