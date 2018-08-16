import Foundation

final class CartActionCreator: ActionCreator  {
    
    static let shared = CartActionCreator()

    var dispatcher: Dispatcher
    var apiClient: APIClient
    
    init(with dispatcher: Dispatcher = .shared, apiClient: APIClient = API.Client.init()) {
        self.dispatcher = dispatcher
        self.apiClient = apiClient
    }

    func addItem(item: Item) {
        dispatcher.dispatch(CartActions.addItem(item: item))
    }

    func removeItem(item: Item) {
        dispatcher.dispatch(CartActions.removeItem(item: item))
    }
    
}
