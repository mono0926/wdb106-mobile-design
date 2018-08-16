import XCTest

protocol Injectable {
  associatedtype Dependency
  init(with dependency: Dependency)
}

protocol APIRequest {
  associatedtype Response
}

protocol APIClient {
  func response<R: APIRequest>(
    from request: R,
    completion: ((R.Response) -> ())?
  )
}

struct MockRequest: APIRequest {
  typealias Response = [String]
}

class MockAPIClient: APIClient {
  func response<R: APIRequest>(
    from request: R,
    completion: ((R.Response) -> ())?) {
    completion?(["test"] as! R.Response)
  }
}

class ViewController: UIViewController, Injectable {
  typealias Dependency = APIClient
  private let apiClient: APIClient

  var items: [String] = []

  required init(with dependency: Dependency) {
    apiClient = dependency
    super.init(nibName: nil, bundle: nil)
  }

  required init?(coder aDecoder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }

  func request() {
    let req = MockRequest()
    apiClient
      .response(from: req) { [weak self] response in
        self?.items = response
      }
  }
}


class DI_Mock_Tests: XCTestCase {

  override func setUp() {
    super.setUp()
  }

  override func tearDown() {
    super.tearDown()
  }

  func testRequest() {
    let apiClient = MockAPIClient()
    let vc = ViewController(with: apiClient)

    XCTAssertTrue(vc.items.isEmpty)
    vc.request()
    XCTAssertEqual(vc.items, ["test"])
  }
}

