//
// Copyright (c) 2025 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
//

import MultiPlatformLibrary

private var koinInstance: Koin!

extension Koin {
    static var instance: Koin {
        return koinInstance
    }

    static func setup() {
        guard koinInstance == nil else {
            fatalError("koin already initialized!")
        }

        let antilog: Antilog?
        #if DEBUG
            antilog = DebugAntilog(defaultTag: "debug")
        #else
            antilog = nil
        #endif

        let koinApp: KoinApplication = KoinKt.startDI(
            antilog: antilog,
            exceptionLogger: CrashlyticsExceptionLogger()
        )

        koinInstance = koinApp.koin
    }
}
