import MultiPlatformLibrary
import MultiPlatformLibraryUnits
import UIKit

extension Repository {
    func toTableUnitItem(
        onClick: @escaping (Repository) -> Void
    ) -> TableUnitItem {

        UITableViewCellUnit<RepositoryItemCell>(
            data: RepositoryItemCell.Data(
                repository: self,
                onClick: onClick
            ),
            itemId: Int64(id.hashValue)
        )
    }
}
