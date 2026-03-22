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
            baseUrl: Environment.Keys.serverBaseUrl.value(),
            antilog: antilog,
            exceptionLogger: CrashlyticsExceptionLogger()
        )

        koinApp.modules(modules: commonIosModule)

        koinInstance = koinApp.koin
    }
}
