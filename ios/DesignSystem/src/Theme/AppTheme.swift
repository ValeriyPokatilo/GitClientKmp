//
// Copyright (c) 2025 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
//

import SwiftUI

public struct AppTheme: Sendable {
    public let colors: AppColors
    public let typography: AppTypography

    public static let shared = AppTheme(
        colors: AppColors.shared,
        typography: AppTypography.shared
    )
}

public struct AppThemeKey: EnvironmentKey {
    public static let defaultValue = AppTheme.shared
}

public extension EnvironmentValues {
    var appTheme: AppTheme {
        get { self[AppThemeKey.self] }
        set { self[AppThemeKey.self] = newValue }
    }
}

public struct AppThemeProvider<Content: View>: View {
    public let content: Content

    public init(@ViewBuilder content: () -> Content) {
        AppTypography.registerCustomFonts()
        self.content = content()
    }

    public var body: some View {
        content
            .environment(\.appTheme, AppTheme.shared)
    }
}
