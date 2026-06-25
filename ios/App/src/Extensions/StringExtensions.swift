import Foundation

extension String {
    func toShortDate() -> String {
        let isoFormatter = ISO8601DateFormatter()
        isoFormatter.formatOptions = [
            .withInternetDateTime,
            .withFractionalSeconds,
        ]

        let date = isoFormatter.date(from: self) ??
            ISO8601DateFormatter().date(from: self)

        guard let date else { return self }

        let calendar = Calendar.current
        let currentYear = calendar.component(.year, from: Date())
        let dateYear = calendar.component(.year, from: date)

        let formatter = DateFormatter()
        formatter.locale = Locale.current

        if dateYear == currentYear {
            formatter.dateFormat = "d MMM"
        } else {
            formatter.dateFormat = "d MMM yyyy"
        }

        return formatter.string(from: date)
    }
}
