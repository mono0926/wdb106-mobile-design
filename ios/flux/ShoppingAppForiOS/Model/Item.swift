import Foundation

struct Item: Equatable, Codable {
    let id: Int
    let price: Int
    let title: String
    let imageUrl: URL
    var inventory: Int
    
    mutating func increase() {
        inventory += 1
    }

    mutating func decrease() {
        inventory = max(0, inventory - 1)
    }
}
