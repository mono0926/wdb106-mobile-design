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
                               imageUrl: URL(string: "https://example.com")!)

    let scheduler = TestScheduler(initialClock: 0)
    var viewModel: ItemListViewModel!

    override func setUp() {
        super.setUp()

        let dependency = ItemListViewModel.Dependency(
            apiClient: MockClient()
        )
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
}
