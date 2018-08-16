import UIKit
import RxSwift

final class ItemListViewModel: Injectable {
    struct Dependency {
        let apiClient: APIClient
    }
    
    private let disposeBag = DisposeBag()

    private let viewWillAppearStream = PublishSubject<Void>()
    private let cartButtonDidTapStream = PublishSubject<Void>()
    private let addItemDidTapStream = PublishSubject<Item>()
    private let itemsStream = BehaviorSubject<[Item]>(value: [])
    private let navigateToCartStream = PublishSubject<Void>()

    // MARK: Injectable

    init(with dependency: Dependency) {
        let apiClient = dependency.apiClient

        viewWillAppearStream
            .flatMapLatest { _ -> Observable<[Item]> in
                let request = API.ItemListRequest()
                return apiClient.response(from: request)
            }
            .bind(to: itemsStream)
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
        return itemsStream.asObservable()
    }

    var navigateToCart: Observable<Void> {
        return navigateToCartStream.asObservable()
    }
}
