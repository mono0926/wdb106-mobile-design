import XCTest
import RxSwift
import RxCocoa
import RxTest
@testable import ShoppingAppForiOS

class ItemStoreTests: XCTestCase {

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
        let store = ItemStore(with: dispatcher)

        let disposeBag = DisposeBag()
        let items = scheduler.createObserver([Item].self)

        store.items
            .bind(to: items)
            .disposed(by: disposeBag)

        scheduler.scheduleAt(5) {
            dispatcher.dispatch(ItemActions.receiveItems(items: [ItemStoreTests.mockItem]))
        }

        scheduler.scheduleAt(10) {
            dispatcher.dispatch(ItemActions.receiveItems(items:
                [ItemStoreTests.mockItem, ItemStoreTests.mockItem, ItemStoreTests.mockItem]
            ))
        }

        scheduler.start()

        let expected = [
            next(0, []),
            next(5, [ItemStoreTests.mockItem]),
            next(10, [ItemStoreTests.mockItem, ItemStoreTests.mockItem, ItemStoreTests.mockItem])
        ]

        XCTAssertEqual(items.events, expected)
    }
}
