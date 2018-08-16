import Foundation
import RxSwift

protocol APIRequest {
    associatedtype Response
    var path: String { get }
}

protocol APIClient {
    func response<Request: APIRequest>(from request: Request) -> Observable<Request.Response>
}

enum API {
    final class Client: APIClient {
        func response<Request: APIRequest>(from request: Request) -> Observable<Request.Response> {
            let urls = [
                "https://gihyo.jp/assets/images/gdp/2017/978-4-7741-9229-1.jpg",
                "https://gihyo.jp/assets/images/gdp/2017/978-4-7741-9392-2.jpg",
                "https://gihyo.jp/assets/images/gdp/2017/978-4-7741-9533-9.jpg",
                "https://gihyo.jp/assets/images/gdp/2018/978-4-7741-9668-8.jpg",
                "https://gihyo.jp/assets/images/gdp/2018/978-4-7741-9792-0.jpg",
                ]
            let mockItems = zip((100...104), urls)
                .map { Item(id: $0.0, price: 1480, title: "WEB+DB PRESS \($0.0)", imageUrl: URL(string: $0.1)!) }
            return Observable.just(mockItems as! Request.Response)
        }
    }

    struct ItemListRequest: APIRequest {
        typealias Response = [Item]
        var path: String {
            return "items/list"
        }
    }
    
}
