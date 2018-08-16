import UIKit

protocol NibInstantiable {
    static var nib: UINib { get }
}

extension NibInstantiable {
    static var nib: UINib {
        return UINib(nibName: String(describing: self), bundle: nil)
    }
}
