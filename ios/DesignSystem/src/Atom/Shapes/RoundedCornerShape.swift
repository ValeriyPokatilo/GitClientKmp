//
// Copyright (c) 2025 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
//

import SwiftUI

public struct RoundedCornerShape: Shape {
    var corner: UIRectCorner
    var radius: CGFloat

    public init(corner: UIRectCorner, radius: CGFloat) {
        self.corner = corner
        self.radius = radius
    }

    public func path(in rect: CGRect) -> Path {
        let path = UIBezierPath(
            roundedRect: rect,
            byRoundingCorners: corner,
            cornerRadii: CGSize(
                width: radius,
                height: radius
            )
        )
        return Path(path.cgPath)
    }
}
