import Foundation

struct Item: Equatable, Codable {
    let id: Int
    let price: Int
    let title: String
    let imageUrl: URL
}
