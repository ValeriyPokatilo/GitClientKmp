import UIKit

class Environment {
    enum Keys: String {
        case serverBaseUrl

        func value() -> String {
            return Environment.bundleDict[self.rawValue] as? String ?? ""
        }
    }

    private static let bundleDict = (Bundle.main.infoDictionary?["Environment"] as? [String: Any] ?? [:])
}
