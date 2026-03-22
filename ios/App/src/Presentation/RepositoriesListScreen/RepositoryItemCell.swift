import MultiPlatformLibrary
import MultiPlatformLibraryUnits
import UIKit

final class RepositoryItemCell: UITableViewCell, Fillable {

    struct Data {
        let repository: Repository
        let onClick: (Repository) -> Void
    }

    typealias DataType = Data

    private var repository: Repository?
    private var onClick: ((Repository) -> Void)?

    @IBOutlet private weak var nameLabel: UILabel!
    @IBOutlet private weak var languageLabel: UILabel!
    @IBOutlet private weak var descriptionLabel: UILabel!

    func fill(_ data: Data) {
        let repo = data.repository

        self.repository = repo
        self.onClick = data.onClick

        nameLabel.text = repo.name
        languageLabel.text = repo.language
        languageLabel.textColor = repo.languageColor.uiColor

        if let description = repo.descriptionText, !description.isEmpty {
            descriptionLabel.text = description
            descriptionLabel.isHidden = false
        } else {
            descriptionLabel.isHidden = true
        }

        selectionStyle = .none
    }

    override func prepareForReuse() {
        super.prepareForReuse()
        nameLabel.text = nil
        languageLabel.text = nil
        descriptionLabel.text = nil
        descriptionLabel.isHidden = false
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        if selected, let repository {
            onClick?(repository)
        }
    }
}
