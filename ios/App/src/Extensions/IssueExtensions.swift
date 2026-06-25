import MultiPlatformLibrary
import MultiPlatformLibraryUnits
import UIKit

extension Issue {
    var stateColor: UIColor {
        switch state {
        case .open:
            return .appLightGreen
        case .closed:
            return .appError
        }
    }

    func toTableUnitItem(
        onClick: @escaping (Issue) -> Void
    ) -> TableUnitItem {
        UITableViewCellUnit<IssueItemCell>(
            data: IssueItemCell.Data(
                issue: self,
                onClick: onClick
            ),
            itemId: Int64(id.hashValue)
        )
    }
}
