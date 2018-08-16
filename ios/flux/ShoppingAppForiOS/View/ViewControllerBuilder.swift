import Foundation

final class ViewControllerBuilder {
    
    static let `default` = ViewControllerBuilder()
    
    let dispatcher: Dispatcher
    let cartStore: CartStore
    let itemStore: ItemStore
    let cartActionCreator: CartActionCreator
    let itemActionCreator: ItemActionCreator

    init(dispatcher: Dispatcher = .shared) {
        self.dispatcher = dispatcher
        
        cartStore = CartStore(with: dispatcher)
        cartActionCreator = CartActionCreator(with: dispatcher)
        itemStore = ItemStore(with: dispatcher)
        itemActionCreator = ItemActionCreator(with: dispatcher)
    }
    
    func makeItemListViewController() -> ItemListViewController {
        let viewModel = ItemListViewModel(
            with: ItemListViewModel.Dependency(
                cartStore: cartStore,
                itemStore: itemStore,
                actionCreator: itemActionCreator
            )
        )
        return ItemListViewController(with: viewModel)
    }
    
    func makeCartViewController() -> CartViewController {
        let viewModel = CartViewModel(
            with: CartViewModel.Dependency(
                store: cartStore,
                actionCreator: cartActionCreator
            )
        )
        return CartViewController(with: viewModel)
    }

}
