import UIKit
import RxSwift
import RxCocoa

final class CartCell: UITableViewCell, NibInstantiable, Reusable {
    typealias Value = CartItem

    @IBOutlet private weak var titleLabel: UILabel!
    @IBOutlet private weak var priceLabel: UILabel!
    @IBOutlet private weak var itemImageView: UIImageView!
    @IBOutlet private weak var quantitysLabel: UILabel!
    @IBOutlet private weak var actionButton: UIButton!

    private let removedItemStream = PublishRelay<Item>()

    var disposeBag = DisposeBag()

    private var value: Value? {
        didSet {
            titleLabel.text = value?.item.title

            quantitysLabel.text = "数量: \(value?.quantity ?? 0)"
            
            if let price = value?.item.price {
                priceLabel.text = "\(price)円+税"
            } else {
                priceLabel.text = "-"
            }

            if let url = value?.item.imageUrl {
                let urlRequest = URLRequest(url: url, cachePolicy: .returnCacheDataElseLoad)
                URLSession.shared.rx.data(request: urlRequest)
                    .map { UIImage(data: $0) }
                    .bind(to: itemImageView!.rx.image)
                    .disposed(by: disposeBag)
            }
        }
    }

    func configure(_ value: Value) {
        self.value = value

        actionButton.rx.tap
            .map { value.item }
            .bind(to: removedItemStream)
            .disposed(by: disposeBag)
    }

    override func prepareForReuse() {
        super.prepareForReuse()
        disposeBag = DisposeBag()
    }
}

// MARK: Output

extension CartCell {
    var removedItem: Observable<Item> {
        return removedItemStream.asObservable()
    }
}
