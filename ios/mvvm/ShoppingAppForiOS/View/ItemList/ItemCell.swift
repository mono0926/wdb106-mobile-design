import UIKit
import RxSwift
import RxCocoa

final class ItemCell: UITableViewCell, NibInstantiable, Reusable {
    typealias Value = Item

    @IBOutlet private weak var titleLabel: UILabel!
    @IBOutlet private weak var priceLabel: UILabel!
    @IBOutlet private weak var itemImageView: UIImageView!
    @IBOutlet private weak var actionButton: UIButton!
    @IBOutlet private weak var inventoryLabel: UILabel!

    private let addedItemStream = PublishRelay<Item>()

    var disposeBag = DisposeBag()

    private var value: Value? {
        didSet {
            titleLabel.text = value?.title

            if let price = value?.price {
                priceLabel.text = "\(price)円+税"
            } else {
                priceLabel.text = "-"
            }

            if let url = value?.imageUrl {
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
            .map { value }
            .bind(to: addedItemStream)
            .disposed(by: disposeBag)
    }

    override func prepareForReuse() {
        super.prepareForReuse()
        disposeBag = DisposeBag()
    }
}

// MARK: Output

extension ItemCell {

    var addedItem: Observable<Item> {
        return addedItemStream.asObservable()
    }
}
