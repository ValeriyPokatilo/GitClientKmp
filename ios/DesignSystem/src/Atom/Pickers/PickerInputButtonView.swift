//
// Copyright (c) 2025 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
//

import SwiftUI

public struct DateRangePickerView: View {
    @Environment(\.appTheme) private var theme

    @Binding private var selectedDateRange: ClosedRange<Date>?
    private let range: any RangeExpression<Date>
    private let flaggedDates: [Date]

    public init(
        selectedDateRange: Binding<ClosedRange<Date>?>,
        range: any RangeExpression<Date> = Date()...,
        flaggedDates: [Date] = []
    ) {
        _selectedDateRange = selectedDateRange
        self.range = range
        self.flaggedDates = flaggedDates
    }

    public var body: some View {
        MultiDatePicker(dateRange: $selectedDateRange, range: range, flaggedDates: flaggedDates)
    }
}

public struct PickerInputButtonView: View {
    @Environment(\.appTheme) private var theme

    private let labelText: String?
    private let selectedDate: Date?
    private let isDateSelected: Bool

    public init(labelText: String? = nil, selectedDate: Date?, isDateSelected: Bool) {
        self.labelText = labelText
        self.selectedDate = selectedDate
        self.isDateSelected = isDateSelected
    }

    public var body: some View {
        content
            .frame(maxWidth: .infinity, alignment: .leading)
            .padding(.horizontal, 16.0)
            .padding(.vertical, 4.0)
            .background(theme.colors.surfaceContainerHigh)
            .cornerRadius(16)
            .overlay(
                RoundedRectangle(cornerRadius: 16)
                    .stroke(
                        isDateSelected ? theme.colors.primaryColor : theme.colors.clear,
                        lineWidth: isDateSelected ? 2 : 0
                    )
            )
    }

    @ViewBuilder
    private var content: some View {
        if let labelText {
            VStack(spacing: .zero) {
                Text(labelText)
                    .font(theme.typography.regular(size: 12))
                    .foregroundColor(theme.colors.onSurfaceColor)

                Text(formattedDate)
                    .font(theme.typography.regular(size: 16))
                    .foregroundColor(theme.colors.onSurfaceColor)
                    .padding(.bottom, 4.0)
            }
        } else {
            Text(formattedDate)
                .font(theme.typography.regular(size: 16))
                .foregroundColor(theme.colors.onSurfaceColor)
                .padding(.vertical, 12.0)
        }
    }

    private var formattedDate: String {
        let formatter = DateFormatter()
        formatter.dateFormat = "d MMMM yyyy"
        return formatter.string(from: selectedDate ?? Date())
    }
}

// MARK: - Previews

// @Previewable доступно только с Xcode 16, поэтому проверяем что мы в Xcode 16 сейчас. Косвенно - по используемому компилятору
#if compiler(>=6)

    @available(iOS 17.0, *)
    #Preview {
        @Previewable @State var previewDate: Date = .init()

        AppThemeProvider {
            DatePickerView(selectedDate: $previewDate)
        }
    }

#endif
