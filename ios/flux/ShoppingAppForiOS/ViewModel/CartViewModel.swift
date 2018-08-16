import UIKit
import RxSwift
import RxCocoa

final class CartViewModel: Injectable {
    struct Dependency {
        let store: CartStore
        let actionCreator: CartActionCreator
    }
    
    private let disposeBag = DisposeBag()

    private let closeButtonDidTapStream = PublishSubject<Void>()
    private let removeItemDidTapStream = PublishSubject<Item>()

    private let dismissStream = PublishSubject<Void>()

    private let cartActionCreator: CartActionCreator
    private let cartStore: CartStore

    init(with dependency: Dependency) {

        cartStore = dependency.store
        cartActionCreator = dependency.actionCreator

        closeButtonDidTapStream
            .bind(to: dismissStream)
            .disposed(by: disposeBag)
        
        removeItemDidTapStream
            .bind(onNext: { [weak self] item in
                self?.cartActionCreator.removeItem(item: item)
            })
            .disposed(by: disposeBag)

        cartStore.items.asObservable()
            .filter { $0.isEmpty }
            .map { _ in }
            .bind(onNext: { [weak self] in
                self?.dismissStream.onNext(())
            })
            .disposed(by: disposeBag)
    }
}

// MARK: Input

extension CartViewModel {
    var closeButtonDidTap: AnyObserver<()> {
        return closeButtonDidTapStream.asObserver()
    }

    var removeButtonDidTap: AnyObserver<Item> {
        return removeItemDidTapStream.asObserver()
    }
}

// MARK: Output

extension CartViewModel {
    var title: Observable<String> {
        return Observable.just("カート")
    }

    var closeButtonTitle: Observable<String> {
        return Observable.just("閉じる")
    }

    var cartItems: Observable<[CartItem]> {
        return cartStore.items.asObservable()
    }

    var totalPrice: Observable<String> {
        return cartStore.totalPrice.map { "合計金額 \($0)円+税" }
    }

    var dismiss: Observable<Void> {
        return dismissStream.asObservable()
    }
}
