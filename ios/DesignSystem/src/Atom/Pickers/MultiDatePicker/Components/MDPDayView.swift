//
// Copyright (c) 2025 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
//

import SwiftUI

/**
 * MDPDayView displays the day of month on a MDPContentView. This a button whose color and
 * selectability is determined from the MDPDayOfMonth in the MDPModel.
 */
struct MDPDayView: View {
    @Environment(\.appTheme) private var theme
    @EnvironmentObject var monthDataModel: MDPModel
    let cellSize: CGFloat = 44
    var dayOfMonth: MDPDayOfMonth

    // filled if selected
    private var fillColor: Color {
        monthDataModel.isSelected(dayOfMonth) ? Color.blue.opacity(0.55) : Color.clear
    }

    // reverse color for selections or gray if not selectable
    private var textColor: Color {
        if dayOfMonth.isToday { return theme.colors.primaryColor }
        if !dayOfMonth.isSelectable { return Color.gray }

        return monthDataModel.isSelected(dayOfMonth) ? theme.colors.primaryColor : Color.black
    }

    private var textFont: Font {
        monthDataModel.isSelected(dayOfMonth) ? theme.typography.bold(size: 16) : theme.typography.medium(size: 16)
    }

    private func handleSelection() {
        if dayOfMonth.isSelectable {
            monthDataModel.selectDay(dayOfMonth)
        }
    }

    var body: some View {
        Button(action: { handleSelection() }) {
            Text("\(dayOfMonth.day)")
                .font(textFont)
                .foregroundColor(textColor)
                .frame(height: cellSize)
                .background(
                    Circle()
                        .fill(
                            monthDataModel.isSelected(dayOfMonth) ? theme.colors.primaryColor.opacity(0.12) : Color.clear
                        )
                        .frame(width: cellSize, height: cellSize)
                )
                .overlay(alignment: .bottom) {
                    if monthDataModel.isFlagged(dayOfMonth) {
                        Circle()
                            .fill(theme.colors.primaryColor)
                            .frame(width: 5, height: 5)
                            .padding(.bottom, 5)
                    }
                }
        }
    }
}

struct DayOfMonthView_Previews: PreviewProvider {
    static var previews: some View {
        MDPDayView(dayOfMonth: MDPDayOfMonth(index: 0, day: 1, date: Date(), isSelectable: true, isToday: false))
            .environmentObject(MDPModel())
    }
}
