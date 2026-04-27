package org.example.library.utils.dateFormatting

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import platform.CoreFoundation.kCFCalendarUnitDay
import platform.CoreFoundation.kCFCalendarUnitHour
import platform.CoreFoundation.kCFCalendarUnitMinute
import platform.CoreFoundation.kCFCalendarUnitMonth
import platform.CoreFoundation.kCFCalendarUnitYear
import platform.Foundation.NSCalendar
import platform.Foundation.NSDate
import platform.Foundation.NSDateComponents

fun LocalDate.toSwiftDate(): NSDate {
    val dateComponents: NSDateComponents = NSDateComponents()
    dateComponents.year = this.year.toLong()
    dateComponents.month = this.monthNumber.toLong()
    dateComponents.day = this.dayOfMonth.toLong()
    val calendar: NSCalendar = NSCalendar.currentCalendar
    // согласно документации
    // Returns nil if the receiver cannot convert the components given in comps into an NSDate object.
    // но у нас точно компоненты корректные, поэтому ситуация null нереалистична
    return requireNotNull(value = calendar.dateFromComponents(comps = dateComponents)) {
        "NSCalendar return nil"
    }
}

fun LocalTime.toSwiftDate(): NSDate {
    val dateComponents: NSDateComponents = NSDateComponents()
    dateComponents.hour = this.hour.toLong()
    dateComponents.minute = this.minute.toLong()
    val calendar: NSCalendar = NSCalendar.currentCalendar
    // согласно документации
    // Returns nil if the receiver cannot convert the components given in comps into an NSDate object.
    // но у нас точно компоненты корректные, поэтому ситуация null нереалистична
    return requireNotNull(value = calendar.dateFromComponents(comps = dateComponents)) {
        "NSCalendar return nil"
    }
}

fun NSDate.toLocalDate(): LocalDate {
    val calendar: NSCalendar = NSCalendar.currentCalendar
    val dateComponents: NSDateComponents = calendar.components(
        unitFlags = kCFCalendarUnitYear or kCFCalendarUnitMonth or kCFCalendarUnitDay,
        fromDate = this
    )

    return LocalDate(
        year = dateComponents.year.toInt(),
        monthNumber = dateComponents.month.toInt(),
        dayOfMonth = dateComponents.day.toInt()
    )
}

fun NSDate.toLocalTime(): LocalTime {
    val calendar: NSCalendar = NSCalendar.currentCalendar
    val dateComponents: NSDateComponents = calendar.components(
        unitFlags = kCFCalendarUnitHour or kCFCalendarUnitMinute,
        fromDate = this
    )

    return LocalTime(
        hour = dateComponents.hour.toInt(),
        minute = dateComponents.minute.toInt()
    )
}
