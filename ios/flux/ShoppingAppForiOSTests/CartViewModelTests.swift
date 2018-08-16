import XCTest
import RxSwift
import RxCocoa
import RxTest
@testable import ShoppingAppForiOS

class CartViewModelTests: XCTestCase {

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
    var viewModel: CartViewModel!

    override func setUp() {
        super.setUp()

        let cartStore = CartStore(with: dispatcher)
        let actionCreator = CartActionCreator(
            with: dispatcher,
            apiClient: MockClient()
        )

        let dependency = CartViewModel.Dependency(
            store: cartStore,
            actionCreator: actionCreator
        )

        viewModel = CartViewModel(with: dependency)
    }

    override func tearDown() {
        super.tearDown()
    }

    func testCartItems() {
        let disposeBag = DisposeBag()

        let cartItems = scheduler.createObserver([CartItem].self)
        let totalPrice = scheduler.createObserver(String.self)
        let dismiss = scheduler.createObserver(Bool.self)

        viewModel.cartItems
            .bind(to: cartItems)
            .disposed(by: disposeBag)

        viewModel.totalPrice
            .bind(to: totalPrice)
            .disposed(by: disposeBag)

        viewModel.dismiss
            .map { _ in true }
            .bind(to: dismiss)
            .disposed(by: disposeBag)


        scheduler.scheduleAt(5) { [unowned self] in
            // Call global event
            self.dispatcher.dispatch(CartActions.addItem(item: ItemStoreTests.mockItem))
        }

        scheduler.scheduleAt(10) { [unowned self] in
            self.viewModel.removeButtonDidTap.onNext(ItemStoreTests.mockItem)
        }

        scheduler.start()

        let expectedCartItems = [
            next(0, []),
            next(5, [CartItem(item: ItemStoreTests.mockItem, quantity: 1)]),
            next(10, [])
        ]

        let expectedTotalPrice = [
            next(0, "合計金額 0円+税"),
            next(5, "合計金額 1000円+税"),
            next(10, "合計金額 0円+税"),
        ]

        let expectedDismiss = [
            next(10, true)
        ]

        XCTAssertEqual(cartItems.events, expectedCartItems)
        XCTAssertEqual(totalPrice.events, expectedTotalPrice)
        XCTAssertEqual(dismiss.events, expectedDismiss)
    }

    func testClose() {
        let disposeBag = DisposeBag()

        let dismiss = scheduler.createObserver(Bool.self)

        viewModel.dismiss
            .map { _ in true }
            .bind(to: dismiss)
            .disposed(by: disposeBag)


        scheduler.scheduleAt(5) { [unowned self] in
            self.viewModel.closeButtonDidTap.onNext(())
        }

        scheduler.start()

        let expectedDismiss = [
            next(5, true)
        ]

        XCTAssertEqual(dismiss.events, expectedDismiss)
    }

}
