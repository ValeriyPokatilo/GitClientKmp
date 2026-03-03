//
// Copyright (c) 2025 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
//

import SwiftUI

/**
 * This component shows a date picker very similar to Apple's SwiftUI 2.0 DatePicker, but with a difference.
 * Instead of just allowing a single date to be picked, the MultiDatePicker also allows the user to select
 * a set of non-contiguous dates or a date range. It just depends on how this View is initialized.
 *
 * init(singleDay: Binding<Date> [,options])
 *      A single-date picker. Selecting a date de-selects the previous selection. Because the binding
 *      is a Date, there is always a selected date.
 *
 * init(anyDates: Binding<[Date]>, [,options])
 *      Allows multiple, non-continguous, dates to be selected. De-select a date by tapping it again.
 *      The binding array may be empty or it will be an array of dates selected in ascending order.
 *
 * init(dateRange: Binding<ClosedRange<Date>?>, [,options])
 *      Selects a date range. Tapping on a date marks it as the first date, tapping a second date
 *      completes the range. Tapping a date again resets the range. The binding will be nil unless
 *      two dates are selected, completeing the range.
 *
 * optional parameters to init() functions are:
 *  - includeDays: .allDays, .weekdaysOnly, .weekendsOnly
 *      Days not selectable are shown in gray and not selected.
 *  - minDate: Date? = nil
 *      Days before minDate are not selectable.
 *  - maxDate: Date? = nil
 *      Days after maxDate are not selectable.
 */
public struct MultiDatePicker: View {
    // the type of picker, based on which init() function is used.
    public enum PickerType {
        case singleDay
        case anyDays
        case dateRange
    }

    @StateObject private var monthModel: MDPModel

    // selects only a single date

    public init(singleDay: Binding<Date?>,
                range: any RangeExpression<Date> = Date()...,
                flaggedDates: [Date] = [])
    {
        _monthModel = StateObject(wrappedValue: MDPModel(singleDay: singleDay, range: range, flaggedDates: flaggedDates))
    }

    // selects any number of dates, non-contiguous

    public init(anyDays: Binding<[Date]>,
                range: any RangeExpression<Date> = Date()...,
                flaggedDates: [Date] = [])
    {
        _monthModel = StateObject(wrappedValue: MDPModel(anyDays: anyDays, range: range, flaggedDates: flaggedDates))
    }

    // selects a closed date range

    public init(
        dateRange: Binding<ClosedRange<Date>?>,
        range: any RangeExpression<Date> = Date()...,
        flaggedDates: [Date] = [],
        onDatesSelected: ((Date?, Date?) -> Void)? = nil
    ) {
        _monthModel = StateObject(
            wrappedValue: MDPModel(
                dateRange: dateRange,
                range: range,
                flaggedDates: flaggedDates,
                onDatesSelected: onDatesSelected
            )
        )
    }

    public var body: some View {
        MDPMonthView()
            .environmentObject(monthModel)
    }
}

struct MultiDatePicker_Previews: PreviewProvider {
    @State static var oneDay: Date? = Date()
    @State static var manyDates = [Date]()
    @State static var dateRange: ClosedRange<Date>? = nil

    static var previews: some View {
        ScrollView {
            VStack {
                MultiDatePicker(singleDay: $oneDay, range: Date()..., flaggedDates: [Date()])
                MultiDatePicker(anyDays: $manyDates, range: ...Date(), flaggedDates: [Date()])
                MultiDatePicker(dateRange: $dateRange, flaggedDates: [Date()])
            }
        }.environment(\.locale, Locale(identifier: "ru"))
    }
}
