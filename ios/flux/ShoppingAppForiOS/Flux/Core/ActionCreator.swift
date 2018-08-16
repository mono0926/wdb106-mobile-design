import Foundation

protocol ActionCreator {
    var dispatcher: Dispatcher { get }
    var apiClient: APIClient { get }
    init(with dispatcher: Dispatcher, apiClient: APIClient)
}
