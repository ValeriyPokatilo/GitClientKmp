import UIKit

final class RepositoryItemCell: UITableViewCell {

    @IBOutlet private weak var nameLabel: UILabel!
    @IBOutlet private weak var languageLabel: UILabel!
    @IBOutlet private weak var descriptionLabel: UILabel!

    func configure(name: String, language: String?, description: String?) {
        nameLabel.text = name
        languageLabel.text = language

        if let description, !description.isEmpty {
            descriptionLabel.text = description
            descriptionLabel.isHidden = false
        } else {
            descriptionLabel.isHidden = true
        }
    }

    override func prepareForReuse() {
        super.prepareForReuse()
        nameLabel.text = nil
        languageLabel.text = nil
        descriptionLabel.text = nil
        descriptionLabel.isHidden = false
    }
}
