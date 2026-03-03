//
// Copyright (c) 2025 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
//

import SwiftUI

/// Многострочное поле ввода на базе TextField от SwiftUI
public struct MultiLineTextFieldView: View {
    @Binding private var text: String
    @FocusState private var isFocused: Bool
    @Environment(\.appTheme) private var theme

    let label: String
    let isDisabled: Bool
    let lineLimit: ClosedRange<Int>
    let onSubmit: () -> Void

    public init(
        text: Binding<String>,
        label: String,
        isDisabled: Bool = false,
        lineLimit: ClosedRange<Int> = 3 ... 6,
        onSubmit: (() -> Void)? = nil
    ) {
        _text = text
        self.label = label
        self.isDisabled = isDisabled
        self.lineLimit = lineLimit
        self.onSubmit = onSubmit ?? {}
    }

    public var body: some View {
        // данные махинации нужны чтобы если логика вне данной view решит что текст должен быть изменен (например ограничение по количеству символов применит) - то мы это изменение корректно отразили
        // комбинация Binding и два onChange
        ZStack(alignment: .topLeading) {
            if text.isEmpty {
                Text(label)
                    .foregroundColor(theme.colors.primaryColor)
                    .font(theme.typography.medium(size: 16))
            }

            TextField(
                "",
                text: $text,
                axis: .vertical
            )
        }
        .frame(maxWidth: .infinity)
        .padding(.vertical, 8)
        .padding(.horizontal)
        .onSubmit(self.onSubmit)
        .lineLimit(lineLimit)
        .colorMultiply(theme.colors.primaryColor)
        .foregroundColor(theme.colors.onSurface)
        .font(theme.typography.medium(size: 16))
        .background(
            RoundedRectangle(cornerRadius: 16)
                .fill(theme.colors.primaryColor)
        )
        .overlay(
            Group {
                if isFocused {
                    RoundedRectangle(cornerRadius: 16)
                        .stroke(theme.colors.primaryColor, lineWidth: 2)
                }
            }
        )
        .disabled(isDisabled)
        .opacity(isDisabled ? 0.3 : 1.0)
        .focused($isFocused)
    }
}

/// Многострочное поле ввода на базе TextEditor от SwiftUI
public struct MultiLineTextEditorView: View {
    @Binding private var text: String
    @State private var localText: String = ""
    @FocusState private var isFocused: Bool
    @Environment(\.appTheme) private var theme

    let label: String
    let isDisabled: Bool
    let lineLimit: ClosedRange<Int>
    let onSubmit: () -> Void

    public init(
        text: Binding<String>,
        label: String,
        isDisabled: Bool = false,
        lineLimit: ClosedRange<Int> = 3 ... 6,
        onSubmit: (() -> Void)? = nil
    ) {
        _text = text
        self.label = label
        self.isDisabled = isDisabled
        self.lineLimit = lineLimit
        self.onSubmit = onSubmit ?? {}
    }

    public var body: some View {
        // данные махинации нужны чтобы если логика вне данной view решит что текст должен быть изменен (например ограничение по количеству символов применит) - то мы это изменение корректно отразили
        // комбинация Binding и два onChange
        TextEditor(
            text: Binding(
                get: { localText },
                set: {
                    localText = $0
                    text = $0
                }
            )
        )
        .onChange(of: localText) { newValue in
            let sourceValue = text
            if sourceValue != newValue {
                localText = sourceValue
            }
        }
        .onChange(of: text) { newValue in
            if localText != newValue {
                localText = newValue
            }
        }
        .onSubmit(self.onSubmit)
        .frame(
            minHeight: max(36, CGFloat(lineLimit.lowerBound) * 24),
            maxHeight: CGFloat(lineLimit.upperBound) * 24
        )
        .focused($isFocused)
        .padding(8)
        .scrollContentBackground(.hidden)
        .foregroundColor(theme.colors.onSurface)
        .font(theme.typography.medium(size: 16))
        .background(
            RoundedRectangle(cornerRadius: 16).fill(theme.colors.primaryColor)
        )
        .overlay(
            Group {
                if isFocused {
                    RoundedRectangle(cornerRadius: 16)
                        .stroke(theme.colors.primaryColor, lineWidth: 2)
                } else if text.isEmpty {
                    Text(label)
                        .foregroundColor(theme.colors.onSurface)
                        .font(theme.typography.medium(size: 16))
                        .padding(.horizontal, 16)
                        .padding(.top, 16)
                        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .topLeading)
                        .allowsHitTesting(false)
                }
            }
        )
        .disabled(isDisabled)
    }
}

// MARK: - Previews

// @Previewable доступно только с Xcode 16, поэтому проверяем что мы в Xcode 16 сейчас. Косвенно - по используемому компилятору
#if compiler(>=6)

    @available(iOS 17.0, *)
    #Preview {
        @Previewable @State var text: String = ""

        ScrollView {
            VStack(spacing: 16) {
                MultiLineTextFieldView(
                    text: $text,
                    label: "Добавьте информацию о шлагбауме или особенностях домофона",
                    isDisabled: false,
                    lineLimit: 3 ... 6
                )

                MultiLineTextEditorView(
                    text: $text,
                    label: "Добавьте информацию о шлагбауме или особенностях домофона",
                    isDisabled: false,
                    lineLimit: 3 ... 6
                )

            }.padding()
        }
    }

#endif
