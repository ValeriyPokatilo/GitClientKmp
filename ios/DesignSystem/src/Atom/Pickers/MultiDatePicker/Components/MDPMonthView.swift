//
// Copyright (c) 2025 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
//

import SwiftUI

/**
 * MDPMonthView is really the crux of the control. This displays everything and handles the interactions
 * and selections. MulitDatePicker is the public interface that sets up the model and this view.
 */
struct MDPMonthView: View {
    @Environment(\.appTheme) private var theme
    @EnvironmentObject var monthDataModel: MDPModel

    @State private var showMonthYearPicker = false
    @State private var testDate = Date()

    private func showPrevMonth() {
        withAnimation {
            monthDataModel.decrMonth()
            showMonthYearPicker = false
        }
    }

    private func showNextMonth() {
        withAnimation {
            monthDataModel.incrMonth()
            showMonthYearPicker = false
        }
    }

    var body: some View {
        VStack {
            HStack {
                MDPMonthYearPickerButton(isPresented: self.$showMonthYearPicker)

                Spacer()

                Button(action: { showPrevMonth() }) {
                    Image(uiImage: .exampleUserIcon)
                        .resizable()
                        .renderingMode(.template)
                        .foregroundStyle(theme.colors.primaryColor)
                        .frame(width: 24, height: 24)
                }
                Button(action: { showNextMonth() }) {
                    Image(uiImage: .exampleUserIcon)
                        .resizable()
                        .renderingMode(.template)
                        .foregroundStyle(theme.colors.primaryColor)
                        .frame(width: 24, height: 24)
                }
            }

            .padding(.horizontal, 16)
            .frame(height: 44)

            if showMonthYearPicker {
                MDPMonthYearPicker(date: monthDataModel.controlDate) { month, year in
                    self.monthDataModel.show(month: month, year: year)
                }
            } else {
                MDPContentView()
            }
        }
    }
}

struct MonthView_Previews: PreviewProvider {
    static var previews: some View {
        MDPMonthView()
            .environmentObject(MDPModel())
    }
}
