//
// Copyright (c) 2025 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
//

import Combine
import SwiftUI

/**
 * This model is used internally by the MultiDatePicker to coordinate what is displayed and what
 * is selected.
 *
 * When the controlDate is set, an array of MDPDayOfMonth objects are created, each representing
 * a day of the controlDate's month/year.
 *
 * The type of selection (single, allDays, dateRange) is determined by which init() method is used
 * for the model.
 *
 * The MDPModel should not be used outside of the MultiDatePicker, which initalizes this model
 * according to the type of selection required.
 */
class MDPModel: NSObject, ObservableObject {
    var formatter: DateFormatter {
        let formatter = DateFormatter()

        formatter.locale = Locale.current

        return formatter
    }

    var calendar = Calendar.current

    // the controlDate determines which month/year is being modeled. whenever it changes it
    // triggers a refresh of the days collection.
    public var controlDate: Date = .init() {
        didSet {
            buildDays()
        }
    }

    // this collection is created whenever the controlDate is changed and reflects the
    // days of the month for the controlDate's month/year
    @Published var days = [MDPDayOfMonth]()

    // once a controlDate is establed, this value will be the formatted Month Year (localized)
    @Published var title = ""

    // this array maintains the selections. for a single date it is an array of 1. for many
    // dates it is an array of those dates. for a date range, it is an array of 2.
    @Published var selections = [Date]()

    // the localized days of the week
    var dayNames: [String] {
        // Меняем первый день недели на понедельник, а не воскресенье, как в оригинальной реализации
        let firstWeekdayIndex = Calendar.current.firstWeekday - 1

        let formatterWeekdays = formatter.shortWeekdaySymbols!

        return Array(formatterWeekdays[firstWeekdayIndex...] + formatterWeekdays[..<firstWeekdayIndex])
    }

    // MARK: - PRIVATE VARS

    // holds the bindings from the app and get updated as the selection changes
    private var singleDayWrapper: Binding<Date?>?
    private var anyDatesWrapper: Binding<[Date]>?
    private var dateRangeWrapper: Binding<ClosedRange<Date>?>?

    // the type of date picker
    private var pickerType: MultiDatePicker.PickerType = .singleDay

    // which days are available for selection
    private var range: any RangeExpression<Date> = Date()...

    // the actual number of days in this calendar month/year (eg, 28 for February)
    private var numDays = 0

    // даты для отображения на ui точной снизу
    private var flaggedDates: [Date] = []

    // Подписка на изменения рэнджа дат, не полностью, а по одной дате
    private var onDatesSelected: ((Date?, Date?) -> Void)?

    // MARK: - INIT

    convenience init(anyDays: Binding<[Date]>,
                     range: any RangeExpression<Date>,
                     flaggedDates: [Date])
    {
        self.init()
        self.anyDatesWrapper = anyDays
        self.range = range
        self.flaggedDates = flaggedDates
        setSelection(anyDays.wrappedValue)

        // set the controlDate to be the first of the anyDays if the
        // anyDays array is not empty.
        if let useDate = anyDays.wrappedValue.first {
            controlDate = useDate
        }
        buildDays()
    }

    convenience init(singleDay: Binding<Date?>,
                     range: any RangeExpression<Date>,
                     flaggedDates: [Date])
    {
        self.init()
        self.singleDayWrapper = singleDay
        self.range = range
        self.flaggedDates = flaggedDates
        setSelection(singleDay.wrappedValue)

        // set the controlDate to be this singleDay
        controlDate = singleDay.wrappedValue ?? Date()
        buildDays()
    }

    convenience init(dateRange: Binding<ClosedRange<Date>?>,
                     range: any RangeExpression<Date>,
                     flaggedDates: [Date],
                     onDatesSelected: ((Date?, Date?) -> Void)? = nil)
    {
        self.init()
        self.dateRangeWrapper = dateRange
        self.range = range
        self.flaggedDates = flaggedDates
        self.onDatesSelected = onDatesSelected
        setSelection(dateRange.wrappedValue)

        // set the selection to be the first in the range if the range exists
        if let dateRange = dateRange.wrappedValue {
            controlDate = dateRange.lowerBound
        }
        buildDays()
    }

    // MARK: - PUBLIC

    func dayOfMonth(byDay: Int) -> MDPDayOfMonth? {
        guard byDay >= 1, byDay <= 31 else { return nil }
        for dom in days {
            if dom.day == byDay {
                return dom
            }
        }
        return nil
    }

    func selectDay(_ day: MDPDayOfMonth) {
        guard day.isSelectable else { return }
        guard let date = day.date else { return }

        switch pickerType {
        // just make the date the new selection
        case .singleDay:
            selections = [date]
            singleDayWrapper?.wrappedValue = date

        // just add the date to the selection list, don't forget to sort it
        case .anyDays:
            if selections.contains(date), let pos = selections.firstIndex(of: date) {
                selections.remove(at: pos)
            } else {
                selections.append(date)
            }
            selections.sort()
            anyDatesWrapper?.wrappedValue = selections

        // if the selection has 1 element, it completes the range else it starts
        // a new range
        default:
            if selections.count != 1 {
                selections = [date]
            } else {
                selections.append(date)
            }
            selections.sort()
            if selections.count == 2 {
                onDatesSelected?(selections.first, selections.last)
                dateRangeWrapper?.wrappedValue = selections[0] ... selections[1]
            } else {
                onDatesSelected?(selections.first, nil)
                dateRangeWrapper?.wrappedValue = nil
            }
        }
    }

    func isSelected(_ day: MDPDayOfMonth) -> Bool {
        guard day.isSelectable else { return false }
        guard let date = day.date else { return false }

        if pickerType == .anyDays || pickerType == .singleDay {
            for test in selections {
                if isSameDay(date1: test, date2: date) {
                    return true
                }
            }
        } else {
            if selections.count == 0 {
                return false
            } else if selections.count == 1 {
                return isSameDay(date1: selections[0], date2: date)
            } else {
                let range = selections[0] ... selections[1]
                return range.contains(date)
            }
        }
        return false
    }

    func isFlagged(_ day: MDPDayOfMonth) -> Bool {
        guard let date = day.date else { return false }

        return flaggedDates.contains { item in
            isSameDay(date1: date, date2: item)
        }
    }

    func incrMonth() {
        if let newDate = calendar.date(byAdding: .month, value: 1, to: controlDate) {
            controlDate = newDate
        }
    }

    func decrMonth() {
        if let newDate = calendar.date(byAdding: .month, value: -1, to: controlDate) {
            controlDate = newDate
        }
    }

    func show(month: Int, year: Int) {
        let components = DateComponents(year: year, month: month, day: 1)
        if let newDate = calendar.date(from: components) {
            controlDate = newDate
        }
    }
}

// MARK: - BUILD DAYS

extension MDPModel {
    private func buildDays() {
        let year = calendar.component(.year, from: controlDate)
        let month = calendar.component(.month, from: controlDate)

        let dateComponents = DateComponents(year: year, month: month)
        let date = calendar.date(from: dateComponents)!

        let range = calendar.range(of: .day, in: .month, for: date)!
        let numDays = range.count

        // Отнимаем 1, чтобы первый день недели был понедельник, а не воскресенье, как в оригинальной реализации
        let ord = calendar.component(.weekday, from: date) - 1
        var index = 0

        let today = Date()

        // create an empty int array
        var daysArray = [MDPDayOfMonth]()

        // for 0 to ord, set the value in the array[index] to be 0, meaning no day here.
        if ord > 1 {
            for _ in 1 ..< ord {
                daysArray.append(MDPDayOfMonth(index: index, day: 0))
                index += 1
            }
        }

        // for index in range, create a DayOfMonth that will represent one of the days
        // in the month. This object needs to be told if it is eligible for selection
        // which is based on the selectionType and min/max dates if present.
        for i in 0 ..< numDays {
            let realDate = calendar.date(from: DateComponents(year: year, month: month, day: i + 1))
            var dom = MDPDayOfMonth(index: index, day: i + 1, date: realDate)
            dom.isToday = isSameDay(date1: today, date2: realDate)
            dom.isSelectable = isEligible(date: realDate)
            daysArray.append(dom)
            index += 1
        }

        // if index is not a multiple of 7, then append 0 to array until the next 7 multiple.
        let total = daysArray.count
        var remainder = 42 - total
        if remainder < 0 {
            remainder = 42 - total
        }

        for _ in 0 ..< remainder {
            daysArray.append(MDPDayOfMonth(index: index, day: 0))
            index += 1
        }

        self.numDays = numDays
        self.title = "\(formatter.standaloneMonthSymbols[month - 1].capitalized) \(year)"
        self.days = daysArray
    }
}

// MARK: - UTILITIES

extension MDPModel {
    private func setSelection(_ date: Date?) {
        pickerType = .singleDay
        selections = [date].compactMap { $0 }
    }

    private func setSelection(_ anyDates: [Date]) {
        pickerType = .anyDays
        selections = anyDates.map { $0 }
    }

    private func setSelection(_ dateRange: ClosedRange<Date>?) {
        pickerType = .dateRange
        if let dateRange = dateRange {
            selections = [dateRange.lowerBound, dateRange.upperBound]
        }
    }

    private func isSameDay(date1: Date?, date2: Date?) -> Bool {
        guard let date1 = date1, let date2 = date2 else { return false }
        let day1 = calendar.component(.day, from: date1)
        let day2 = calendar.component(.day, from: date2)
        let year1 = calendar.component(.year, from: date1)
        let year2 = calendar.component(.year, from: date2)
        let month1 = calendar.component(.month, from: date1)
        let month2 = calendar.component(.month, from: date2)
        return (day1 == day2) && (month1 == month2) && (year1 == year2)
    }

    private func isEligible(date: Date?) -> Bool {
        guard let date = date else { return true }

        let onlyDate = calendar.startOfDay(for: date) // Обнуляем время

        if let range = range as? ClosedRange<Date> {
            return onlyDate >= calendar.startOfDay(for: range.lowerBound) &&
                onlyDate <= calendar.startOfDay(for: range.upperBound)
        } else if let range = range as? Range<Date> {
            return onlyDate >= calendar.startOfDay(for: range.lowerBound) &&
                onlyDate < calendar.startOfDay(for: range.upperBound)
        } else if let range = range as? PartialRangeFrom<Date> {
            return onlyDate >= calendar.startOfDay(for: range.lowerBound)
        } else if let range = range as? PartialRangeUpTo<Date> {
            return onlyDate < calendar.startOfDay(for: range.upperBound)
        } else if let range = range as? PartialRangeThrough<Date> {
            return onlyDate <= calendar.startOfDay(for: range.upperBound)
        } else {
            return false // Если передан неизвестный тип
        }
    }
}
