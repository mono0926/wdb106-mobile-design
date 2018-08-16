import UIKit

final class CartHeaderView: UIView, NibInstantiable {

    @IBOutlet private weak var priceLabel: UILabel!

    static func make() -> CartHeaderView {
        let view = CartHeaderView.nib.instantiate(withOwner: nil, options: nil)[0] as! CartHeaderView
        return view
    }

    func configure(_ totalPrice: String) {
        self.priceLabel.text = totalPrice
    }
}
