import MultiPlatformLibrary
import MultiPlatformLibraryUnits
import UIKit

final class IssueItemCell: UITableViewCell, Fillable {
    struct Data {
        let issue: Issue
        let onClick: (Issue) -> Void
    }

    typealias DataType = Data

    private var issue: Issue?
    private var onClick: ((Issue) -> Void)?

    @IBOutlet private var titleLabel: UILabel!
    @IBOutlet private var statusLabel: UILabel!
    @IBOutlet private var updatedAtLabel: UILabel!

    func fill(_ data: Data) {
        let issue = data.issue

        self.issue = issue
        self.onClick = data.onClick

        titleLabel.text = issue.title
        statusLabel.text = issue.state.displayText()
        statusLabel.textColor = issue.stateColor
        updatedAtLabel.text = issue.updatedAt.toShortDate()

        selectionStyle = .none
    }

    override func prepareForReuse() {
        super.prepareForReuse()
        titleLabel.text = nil
        statusLabel.text = nil
        updatedAtLabel.text = nil
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        if selected, let issue {
            onClick?(issue)
        }
    }
}
