//
// Copyright (c) 2025 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
//

import SwiftUI

/**
 * The MDPMonthYearPickerButton sits at the top of the MDPMonthView and displays the current month
 * and year showing in the view. Tapping this control switches the main view to the month/year
 * picker.
 *
 * This is a quick way for the user to jump the year or month without having to the < or >
 * buttons.
 */
struct MDPMonthYearPickerButton: View {
    @Environment(\.appTheme) private var theme
    @EnvironmentObject var monthDataModel: MDPModel

    @Binding var isPresented: Bool

    var body: some View {
        Button(action: { withAnimation { isPresented.toggle() } }) {
            HStack {
                Text(monthDataModel.title)
                    .font(theme.typography.medium(size: 16))
                    .foregroundColor(theme.colors.onSurface)
                Image(uiImage: .exampleUserIcon)
                    .resizable()
                    .renderingMode(.template)
                    .foregroundStyle(theme.colors.primaryColor)
                    .frame(width: 22, height: 22)
                    .rotationEffect(self.isPresented ? .degrees(90) : .degrees(0))
            }
        }
    }
}

struct MonthYearPickerButton_Previews: PreviewProvider {
    static var previews: some View {
        MDPMonthYearPickerButton(isPresented: .constant(false))
    }
}
