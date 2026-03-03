//
// Copyright (c) 2025 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
//

import SwiftUI

public struct AlertView: View {
    @Environment(\.appTheme) var theme

    let title: String
    let message: String
    let buttons: [AlertButton]

    @State private var appeared: Bool = false

    public init(
        title: String,
        message: String,
        buttons: [AlertButton]
    ) {
        self.title = title
        self.message = message
        self.buttons = buttons
    }

    public var body: some View {
        ZStack {
            Color.clear

            if appeared {
                PaddingView {
                    VStack(spacing: 0) {
                        titleView
                        messageView
                            .padding(.bottom, 15)

                        Divider()

                        ForEach(buttons, id: \.title) { data in
                            actionButton(data)
                                .clipped()
                        }
                    }
                }
                .padding(.horizontal, 64)
                .transition(.opacity)
            }
        }.onAppear {
            // так как у нас на уровне transaction при показе cover выключаются
            // анимации - делаем анимацию отдельно, через вот этот флаг appeared
            // и withAnimation
            withAnimation {
                appeared = true
            }
        }
    }
}

private struct PaddingView<Content: View>: View {
    @Environment(\.appTheme) var theme
    let content: () -> Content

    var body: some View {
        VStack {
            content()
        }
        .background(theme.colors.primaryColor)
        .cornerRadius(16)
    }
}

private extension AlertView {
    var titleView: some View {
        Text(title)
            .font(.system(size: 17, weight: .semibold))
            .fixedSize(horizontal: false, vertical: true)
            .padding(.top, 19)
            .padding(.horizontal, 16)
            .multilineTextAlignment(.center)
            .foregroundColor(theme.colors.primaryColor)
    }

    var messageView: some View {
        Text(message)
            .font(.system(size: 13, weight: .regular))
            .fixedSize(horizontal: false, vertical: true)
            .multilineTextAlignment(.center)
            .padding(.top, 8)
            .padding(.horizontal, 16)
            .foregroundColor(theme.colors.primaryColor)
    }

    @ViewBuilder
    func actionButton(_ data: AlertButton) -> some View {
        Button(action: data.action) {
            Text(data.title)
                .foregroundColor(data.isDestructive ? Color.red :
                    (data.primary ? theme.colors.primaryColor : theme.colors.onSurface))
                .font(.system(size: 17, weight: .semibold))
                .frame(height: 44)
                // важно чтобы этот модификатор был ДО бекграунда, иначе кнопка будет
                // не на всю ширину
                .frame(maxWidth: .infinity)
                .background(data.primary ? theme.colors.primaryColor : nil)
        }
    }
}

public extension View {
    func appAlert(
        data: Binding<AlertData?>
    ) -> some View {
        self.modifier(AppAlertModifier(data: data))
    }
}

struct AppAlertModifier: ViewModifier {
    let data: Binding<AlertData?>

    func body(content: Content) -> some View {
        content
            // не использовать .fullScreenCover
            // иначе возникают баги при показе алерта поверх .sheet
            .overlay {
                if let data = data.wrappedValue {
                    ZStack {
                        Color.black
                            .opacity(0.5)
                            .ignoresSafeArea()
                            .onTapGesture { self.data.wrappedValue = nil }

                        AlertView(
                            title: data.title,
                            message: data.message,
                            buttons: data.buttons
                        )
                    }
                }
            }
    }
}

public struct AlertData {
    let title: String
    let message: String
    let buttons: [AlertButton]

    public init(
        title: String,
        message: String,
        buttons: [AlertButton] = []
    ) {
        self.title = title
        self.message = message
        self.buttons = buttons
    }
}

public struct AlertButton {
    let title: String
    let primary: Bool
    let isDestructive: Bool
    let action: () -> Void

    public init(
        title: String,
        primary: Bool = false,
        isDestructive: Bool = false,
        action: @escaping () -> Void
    ) {
        self.title = title
        self.primary = primary
        self.isDestructive = isDestructive
        self.action = action
    }
}
