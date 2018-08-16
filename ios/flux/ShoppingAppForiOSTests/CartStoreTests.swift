import XCTest
import RxSwift
import RxCocoa
import RxTest
@testable import ShoppingAppForiOS

class CartStoreTests: XCTestCase {

    static let mockItem = Item(id: 0,
                               price: 1000,
                               title: "title",
                               imageUrl: URL(string: "https://example.com")!,
                               inventory: 10)

    let scheduler = TestScheduler(initialClock: 0)

    override func setUp() {
        super.setUp()
    }

    override func tearDown() {
        super.tearDown()
    }

    func testItems() {

        let dispatcher = Dispatcher()
        let store = CartStore(with: dispatcher)

        let disposeBag = DisposeBag()
        let cartItems = scheduler.createObserver([CartItem].self)
        let totalPrice = scheduler.createObserver(Int.self)
        let totalCount = scheduler.createObserver(Int.self)

        store.items
            .bind(to: cartItems)
            .disposed(by: disposeBag)

        store.totalPrice
            .bind(to: totalPrice)
            .disposed(by: disposeBag)

        store.totalCount
            .bind(to: totalCount)
            .disposed(by: disposeBag)

        scheduler.scheduleAt(5) {
            dispatcher.dispatch(CartActions.addItem(item: ItemStoreTests.mockItem))
        }

        scheduler.scheduleAt(10) {
            dispatcher.dispatch(CartActions.addItem(item: ItemStoreTests.mockItem))
        }

        scheduler.scheduleAt(15) {
            dispatcher.dispatch(CartActions.removeItem(item: ItemStoreTests.mockItem))
            dispatcher.dispatch(CartActions.removeItem(item: ItemStoreTests.mockItem))
        }

        scheduler.start()

        let expectedCartItems = [
            next(0, []),
            next(5, [CartItem(item: ItemStoreTests.mockItem, quantity: 1)]),
            next(10, [CartItem(item: ItemStoreTests.mockItem, quantity: 2)]),
            next(15, [CartItem(item: ItemStoreTests.mockItem, quantity: 1)]),
            next(15, []),
        ]

        let expectedTotalPrice = [
            next(0, 0),
            next(5, 1000),
            next(10, 2000),
            next(15, 1000),
            next(15, 0)
        ]

        let expectedTotalCount = [
            next(0, 0),
            next(5, 1),
            next(10, 2),
            next(15, 1),
            next(15, 0)
        ]

        XCTAssertEqual(cartItems.events, expectedCartItems)
        XCTAssertEqual(totalPrice.events, expectedTotalPrice)
        XCTAssertEqual(totalCount.events, expectedTotalCount)
    }
}

