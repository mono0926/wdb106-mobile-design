import Foundation
import RxSwift

final class ItemActionCreator: ActionCreator {

    static let shared = ItemActionCreator()
    
    var dispatcher: Dispatcher
    var apiClient: APIClient

    private let disposeBag = DisposeBag()

    init(with dispatcher: Dispatcher = .shared, apiClient: APIClient = API.Client()) {
        self.dispatcher = dispatcher
        self.apiClient = apiClient
    }
    
    func fetchItems() {
        
        // Request start
        dispatcher.dispatch(ItemActions.requestItems)
        
        apiClient.response(from: API.ItemListRequest())
            .subscribe(
                onNext: { [weak self] (items) in
                    // Request succeed
                    self?.dispatcher.dispatch(ItemActions.receiveItems(items: items))
                },
                onError: { [weak self] (error) in
                    // Request failed
                    self?.dispatcher.dispatch(ItemActions.receiveError(error: error))
                }
            )
            .disposed(by: disposeBag)
    }
    
    func addItemToCart(item: Item) {
        dispatcher.dispatch(CartActions.addItem(item: item))
    }

}
