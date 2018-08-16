import Foundation

struct CartItem: Equatable, Codable {
    let item: Item
    var quantity: Int
    
    mutating func increase() {
        quantity += 1
    }
    
    mutating func decrease() {
        quantity = max(0, quantity - 1)
    }
}
