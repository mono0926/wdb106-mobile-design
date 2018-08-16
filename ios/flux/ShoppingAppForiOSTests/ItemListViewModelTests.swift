import XCTest
import RxSwift
import RxCocoa
import RxTest
@testable import ShoppingAppForiOS

class ItemListViewModelTests: XCTestCase {

    final class MockClient: ShoppingAppForiOS.APIClient {
        func response<Request: ShoppingAppForiOS.APIRequest>(from request: Request) -> Observable<Request.Response> {
            let mockItems = [ItemListViewModelTests.mockItem]
            return Observable.just(mockItems as! Request.Response)
        }
    }

    static let mockItem = Item(id: 0,
                               price: 1000,
                               title: "title",
                               imageUrl: URL(string: "https://example.com")!,
                               inventory: 10)

    let scheduler = TestScheduler(initialClock: 0)
    let dispatcher = Dispatcher()
    var viewModel: ItemListViewModel!

    override func setUp() {
        super.setUp()

        let itemStore = ItemStore(with: dispatcher)
        let cartStore = CartStore(with: dispatcher)
        let actionCreator = ItemActionCreator(with: dispatcher, apiClient: MockClient())

        let dependency = ItemListViewModel.Dependency(cartStore: cartStore,
                                                      itemStore: itemStore,
                                                      actionCreator: actionCreator)
        viewModel = ItemListViewModel(with: dependency)
    }

    override func tearDown() {
        super.tearDown()
    }

    func testViewWillAppear() {
        let disposeBag = DisposeBag()
        let items = scheduler.createObserver([Item].self)

        viewModel.items
            .bind(to: items)
            .disposed(by: disposeBag)

        scheduler.scheduleAt(5) { [unowned self] in
            self.viewModel.viewWillAppear.onNext(())
        }

        scheduler.start()

        let expectedItems = [
            next(0, []),
            next(5, [ItemListViewModelTests.mockItem])
        ]

        XCTAssertEqual(items.events, expectedItems)
    }

    func testCartItemsLabel() {
        let disposeBag = DisposeBag()
        let cartLabel = scheduler.createObserver(String.self)

        viewModel.cartItemsLabel
            .bind(to: cartLabel)
            .disposed(by: disposeBag)

        scheduler.scheduleAt(5) { [unowned self] in
            self.viewModel.addButtonDidTap.onNext(ItemStoreTests.mockItem)
        }

        scheduler.scheduleAt(10) { [unowned self] in
            // Call global event
            self.dispatcher.dispatch(CartActions.removeItem(item: ItemStoreTests.mockItem))
        }

        scheduler.start()

        let expectedCartItemsLabel = [
            next(0, "カート"),
            next(5, "カート(1)"),
            next(10, "カート"),
            ]

        XCTAssertEqual(cartLabel.events, expectedCartItemsLabel)
    }

    func testNavigateToCart() {
        let disposeBag = DisposeBag()
        let navigateToCart = scheduler.createObserver(Bool.self)

        viewModel.navigateToCart
            .map { _ in true }
            .bind(to: navigateToCart)
            .disposed(by: disposeBag)

        scheduler.scheduleAt(5) { [unowned self] in
            self.viewModel.cartButtonDidTap.onNext(())
        }

        scheduler.start()
        XCTAssertEqual(navigateToCart.events, [next(5, true)])
    }

    func testScenario() {
        let disposeBag = DisposeBag()
        let itemInventories = scheduler.createObserver([Int].self)
        let cartLabel = scheduler.createObserver(String.self)
        let navigateToCart = scheduler.createObserver(Bool.self)

        viewModel.items
            .map { $0.map { $0.inventory } }
            .bind(to: itemInventories)
            .disposed(by: disposeBag)

        viewModel.cartItemsLabel
            .bind(to: cartLabel)
            .disposed(by: disposeBag)

        viewModel.navigateToCart
            .map { _ in true }
            .bind(to: navigateToCart)
            .disposed(by: disposeBag)

        scheduler.scheduleAt(5) { [unowned self] in
            self.viewModel.viewWillAppear.onNext(())
        }

        scheduler.scheduleAt(10) { [unowned self] in
            self.viewModel.addButtonDidTap.onNext(ItemStoreTests.mockItem)
        }

        scheduler.scheduleAt(15) { [unowned self] in
            // Call global event
            self.dispatcher.dispatch(CartActions.removeItem(item: ItemStoreTests.mockItem))
        }

        scheduler.scheduleAt(20) { [unowned self] in
            self.viewModel.cartButtonDidTap.onNext(())
        }

        scheduler.start()

        let expectedItemInventory = [
            next(0, []),
            next(5, [10]),
            next(10, [9]),
            next(15, [10]),
        ]

        let expectedCartItemsLabel = [
            next(0, "カート"),
            next(10, "カート(1)"),
            next(15, "カート"),
        ]

        let expectedNavigateToCart = [next(20, true)]

        XCTAssertEqual(itemInventories.events, expectedItemInventory)
        XCTAssertEqual(cartLabel.events, expectedCartItemsLabel)
        XCTAssertEqual(navigateToCart.events, expectedNavigateToCart)
    }
}
