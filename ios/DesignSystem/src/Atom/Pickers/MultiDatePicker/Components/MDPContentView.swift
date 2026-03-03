//
// Copyright (c) 2025 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
//

import SwiftUI

/**
 * Displays the calendar of MDPDayOfMonth items using MDPDayView views.
 */
struct MDPContentView: View {
    @EnvironmentObject var monthDataModel: MDPModel

    // Если не ширину нужно изменить, переопределяйте этот параметр
    var maxWidth: CGFloat = UIScreen.main.bounds.width - 32.0

    private var cellSpacing: CGFloat {
        maxWidth / (6 + 7 * (44 / 5.83))
    }

    private var cellSize: CGFloat {
        (44 / 5.83) * cellSpacing
    }

    var columns: [GridItem] {
        [
            GridItem(.fixed(cellSize), spacing: cellSpacing),
            GridItem(.fixed(cellSize), spacing: cellSpacing),
            GridItem(.fixed(cellSize), spacing: cellSpacing),
            GridItem(.fixed(cellSize), spacing: cellSpacing),
            GridItem(.fixed(cellSize), spacing: cellSpacing),
            GridItem(.fixed(cellSize), spacing: cellSpacing),
            GridItem(.fixed(cellSize), spacing: cellSpacing),
        ]
    }

    var body: some View {
        LazyVGrid(columns: columns, spacing: cellSpacing) {
            // Sun, Mon, etc.
            ForEach(0 ..< monthDataModel.dayNames.count, id: \.self) { index in
                Text(monthDataModel.dayNames[index].uppercased())
                    .font(.caption)
                    .foregroundColor(.gray)
            }
            .padding(.bottom, 10)

            // The actual days of the month.
            ForEach(0 ..< monthDataModel.days.count, id: \.self) { index in
                if monthDataModel.days[index].day == 0 {
                    Text("")
                        .frame(minHeight: cellSize, maxHeight: cellSize)
                } else {
                    MDPDayView(dayOfMonth: monthDataModel.days[index])
                }
            }
        }.padding(.bottom, 10)
    }
}

struct MonthContentView_Previews: PreviewProvider {
    static var previews: some View {
        GeometryReader { proxy in
            MDPContentView(maxWidth: proxy.size.width)
        }
    }
}
