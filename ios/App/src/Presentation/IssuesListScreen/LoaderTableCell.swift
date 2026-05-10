import MultiPlatformLibraryUnits
import NVActivityIndicatorView
import UIKit

final class LoaderTableCell: UITableViewCell, Fillable {
    typealias DataType = Void

    @IBOutlet private var indicatorView: NVActivityIndicatorView!

    func fill(_: Void) {
        indicatorView.type = .circleStrokeSpin
        indicatorView.startAnimating()
    }
}
