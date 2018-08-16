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
                               imageUrl: URL(string: "https://example.com")!)

    let scheduler = TestScheduler(initialClock: 0)
    var viewModel: CartViewModel!

    override func setUp() {
        super.setUp()
        viewModel = CartViewModel(with: ())
    }

    override func tearDown() {
        super.tearDown()
    }

    func testLabels() {
        let disposeBag = DisposeBag()

        let title = scheduler.createObserver(String.self)
        let closeButtonTitle = scheduler.createObserver(String.self)

        viewModel.title
            .bind(to: title)
            .disposed(by: disposeBag)

        viewModel.closeButtonTitle
            .bind(to: closeButtonTitle)
            .disposed(by: disposeBag)

        scheduler.start()

        let expectedTitle = [
            next(0, "カート"), completed(0)
        ]

        let expectedCloseButtonTitle = [
            next(0, "閉じる"), completed(0)
        ]

        XCTAssertEqual(title.events, expectedTitle)
        XCTAssertEqual(closeButtonTitle.events, expectedCloseButtonTitle)
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
