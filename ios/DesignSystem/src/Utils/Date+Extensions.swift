//
// Copyright (c) 2025 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
//

import Foundation

public extension RangeExpression where Bound == Date {
    func getUpperBound() -> Date? {
        if let closedRange = self as? ClosedRange<Date> {
            return closedRange.upperBound
        } else if let partialRangeThrough = self as? PartialRangeThrough<Date> {
            return partialRangeThrough.upperBound
        } else {
            return nil
        }
    }

    func getLowerBound() -> Date? {
        if let closedRange = self as? ClosedRange<Date> {
            return closedRange.lowerBound
        } else if let partialRangeFrom = self as? PartialRangeFrom<Date> {
            return partialRangeFrom.lowerBound
        } else {
            return nil
        }
    }
}
