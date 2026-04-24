//
// Copyright (c) 2025 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
//

import MultiPlatformLibrary
import SwiftUI

@main
struct MobileApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate

    var body: some Scene {
        WindowGroup {
            ZStack {
                Color.white.ignoresSafeArea()
            }
        }
    }
}
