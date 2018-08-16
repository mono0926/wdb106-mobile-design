import UIKit
import RxSwift

final class ItemListViewModel: Injectable {
    struct Dependency {
        let cartStore: CartStore
        let itemStore: ItemStore
        let actionCreator: ItemActionCreator
    }
    
    private let disposeBag = DisposeBag()

    private let viewWillAppearStream = PublishSubject<Void>()
    private let cartButtonDidTapStream = PublishSubject<Void>()
    private let addItemDidTapStream = PublishSubject<Item>()
    private let navigateToCartStream = PublishSubject<Void>()
    private let itemsStream = BehaviorSubject<[Item]>(value: [])
    
    private let itemActionCreator: ItemActionCreator
    private let itemStore: ItemStore
    private let cartStore: CartStore

    // MARK: Injectable

    init(with dependency: Dependency) {
        itemActionCreator = dependency.actionCreator
        itemStore = dependency.itemStore
        cartStore = dependency.cartStore

        viewWillAppearStream
            .take(1)
            .bind(onNext: { [weak self] item in
                self?.itemActionCreator.fetchItems()
            })
            .disposed(by: disposeBag)
        
        addItemDidTapStream
            .bind(onNext: { [weak self] item in
                self?.itemActionCreator.addItemToCart(item: item)
            })
            .disposed(by: disposeBag)
        
        cartButtonDidTapStream
            .bind(to: navigateToCartStream)
            .disposed(by: disposeBag)
    }
}

// MARK: Input

extension ItemListViewModel {
    var viewWillAppear: AnyObserver<()> {
        return viewWillAppearStream.asObserver()
    }

    var cartButtonDidTap: AnyObserver<()> {
        return cartButtonDidTapStream.asObserver()
    }

    var addButtonDidTap: AnyObserver<Item> {
        return addItemDidTapStream.asObserver()
    }

}

// MARK: Output

extension ItemListViewModel {
    var title: Observable<String> {
        return Observable.just("商品リスト")
    }

    var cartButtonTitle: Observable<String> {
        return Observable.just("カート")
    }

    var items: Observable<[Item]> {
        return itemStore.items.asObservable()
    }

    var isEnableCartButton: Observable<Bool> {
        return cartStore.items.map { !$0.isEmpty }
    }

    var cartItemsLabel: Observable<String> {
        return cartStore.totalCount.map {
            return $0 == 0 ? "カート" : "カート(\($0))"
        }
    }

    var navigateToCart: Observable<Void> {
        return navigateToCartStream.asObservable()
    }
}
