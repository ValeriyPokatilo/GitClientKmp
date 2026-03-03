//
// Copyright (c) 2025 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
//

import SwiftUI

public struct AppTypography: Sendable {
    public let headerH1: Font
    public let headerH2: Font
    public let headerH3: Font
    public let largeTitle: Font
    public let description: Font
    public let descriptionD1: Font
    public let descriptionD2: Font
    public let descriptionD1Medium: Font
    public let button: Font
    public let regular: Font
    public let segmentButton: Font
    public let textFieldError: Font

    public static let shared = AppTypography(
        headerH1: Font.custom("Manrope-ExtraBold", size: 32),
        headerH2: Font.custom("Manrope-Bold", size: 30),
        headerH3: Font.custom("Manrope-SemiBold", size: 24),
        largeTitle: Font.custom("Manrope-Bold", size: 18),
        description: Font.custom("Manrope-Regular", size: 16),
        descriptionD1: Font.custom("Manrope-Medium", size: 16),
        descriptionD2: Font.custom("Manrope-Medium", size: 14),
        descriptionD1Medium: Font.custom("Manrope-Medium", size: 16),
        button: Font.custom("Manrope-Bold", size: 16),
        regular: Font.custom("Manrope-Regular", size: 14),
        segmentButton: Font.custom("Manrope-SemiBold", size: 16),
        textFieldError: Font.custom("Manrope-Regular", size: 12)
    )

    public func regular(size: CGFloat) -> Font {
        return Font.custom("Manrope-Regular", size: size)
    }

    public func medium(size: CGFloat) -> Font {
        return Font.custom("Manrope-Medium", size: size)
    }

    public func semiBold(size: CGFloat) -> Font {
        return Font.custom("Manrope-SemiBold", size: size)
    }

    public func bold(size: CGFloat) -> Font {
        return Font.custom("Manrope-Bold", size: size)
    }

    public func extraBold(size: CGFloat) -> Font {
        return Font.custom("Manrope-ExtraBold", size: size)
    }

    public func regular(size: CGFloat) -> UIFont {
        return UIFont(name: "Manrope-Regular", size: size)!
    }

    public func medium(size: CGFloat) -> UIFont {
        return UIFont(name: "Manrope-Medium", size: size)!
    }

    public func semiBold(size: CGFloat) -> UIFont {
        return UIFont(name: "Manrope-SemiBold", size: size)!
    }

    public func bold(size: CGFloat) -> UIFont {
        return UIFont(name: "Manrope-Bold", size: size)!
    }

    /// Регистрация кастомных шрифтов для использования.
    /// Данная функция нужна чтобы работали шрифты и в приложении и в SwiftUI Preview,
    /// потому что для превью нужно грузить шрифты из Bundle фреймворка, который можно
    /// получить надежно только через ObjC функцию.
    internal static func registerCustomFonts() {
        let bundle: Bundle = DSResourcesBundle.getBundle()!
        let fonts: [String] = [
            "Manrope-Bold.ttf",
            "Manrope-ExtraBold.ttf",
            "Manrope-ExtraLight.ttf",
            "Manrope-Light.ttf",
            "Manrope-Medium.ttf",
            "Manrope-Regular.ttf",
            "Manrope-SemiBold.ttf",
        ]
        for font in fonts {
            guard let url = bundle.url(forResource: font, withExtension: nil) else { return }
            CTFontManagerRegisterFontsForURL(url as CFURL, .process, nil)
        }
    }
}
