//
// Copyright (c) 2025 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
//

import SwiftUI
import UIKit

public struct AppColors: Sendable {
    public let primaryColor: Color
    public let surface: Color
    public let onSurface: Color
    public let shadowColor: Color

    public static let shared = AppColors(
        primaryColor: Color(hex: 0xA577FE),
        surface: Color(hex: 0xF4F6F9),
        onSurface: Color(hex: 0x1E3660),
        shadowColor: Color(hex: 0x2F20_4B0D, opacity: 0.05)
    )
}
