import FirebaseCrashlytics
import Foundation
import MultiPlatformLibrary

public class CrashlyticsExceptionLogger: ExceptionLogger {
    public func recordException(throwable: KotlinThrowable) {
        let exceptionModel = ExceptionModel(
            name: CrashReportingCore.shared.getExceptionName(throwable: throwable),
            reason: throwable.message ?? "-"
        )
        let stackTrace = CrashReportingCore.shared.getStackTrace(throwable: throwable)
        let stackFrames = stackTrace.map {
            StackFrame(address: $0.uintValue)
        }

        exceptionModel.stackTrace = stackFrames

        Crashlytics.crashlytics().record(exceptionModel: exceptionModel)
    }

    public func setUserId(userId: String) {
        Crashlytics.crashlytics().setUserID(userId)
    }

    public func setCustomValue(value: String, forKey: String) {
        Crashlytics.crashlytics().setCustomValue(value, forKey: forKey)
    }

    public func log(message: String) {
        Crashlytics.crashlytics().log(message)
    }
}
