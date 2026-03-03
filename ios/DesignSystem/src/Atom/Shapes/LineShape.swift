//
// Copyright (c) 2025 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
//

import SwiftUI

public struct LineShape: Shape {
    public func path(in rect: CGRect) -> Path {
        var path = Path()
        path.move(to: .zero)
        path.addLine(
            to: CGPoint(x: rect.width, y: 0)
        )
        return path
    }
}
