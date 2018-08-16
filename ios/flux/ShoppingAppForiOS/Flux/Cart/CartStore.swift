import Foundation
import RxSwift
import RxCocoa

final class CartStore: Store {
    
    private let disposeBag = DisposeBag()
    private let itemsStream = BehaviorRelay<[CartItem]>(value: [])
    
    init(with dispatcher: Dispatcher = .shared) {

        dispatcher.register { [weak self] (action: CartActions) in
            guard let `self` = self else { return }
            switch action {
            case .addItem(let item):
                self.add(item)
            case .removeItem(let item):
                self.remove(item)
            }
        }
        .disposed(by: disposeBag)

    }

    // MARK: Output

    var items: Observable<[CartItem]> {
        return itemsStream.asObservable()
    }

    var totalPrice: Observable<Int> {
        return itemsStream.asObservable()
            .map { $0.map { $0.quantity * $0.item.price }.reduce(0, +) }
    }
    
    var totalCount: Observable<Int> {
        return itemsStream.asObservable()
            .map { $0.map { $0.quantity }.reduce(0, +) }
    }
    
    // MARK: Mutation
    private func add(_ item: Item) {
        // Add new item
        guard var cartItem = itemsStream.value.first(where: { $0.item.id == item.id } ) else {
            let cartItem = CartItem(item: item, quantity: 1)
            itemsStream.accept(itemsStream.value + [cartItem])
            return
        }
        // Update stock if already exists
        let newItems = itemsStream.value.map { (value) -> CartItem in
            if value.item.id == cartItem.item.id {
                cartItem.increase()
                return cartItem
            }
            return value
            
        }
        itemsStream.accept(newItems)
    }

    private func remove(_ item: Item) {
        guard var cartItem = itemsStream.value.first(where: { $0.item.id == item.id } ) else {
            return
        }
        
        if cartItem.quantity == 1 {
            itemsStream.accept(itemsStream.value.filter {$0.item.id != cartItem.item.id })
            return
        }
        
        let newItems = itemsStream.value.map { (value) -> CartItem in
            if value.item.id == cartItem.item.id {
                cartItem.decrease()
                return cartItem
            }
            return value
            
        }
        itemsStream.accept(newItems)
    }

}
