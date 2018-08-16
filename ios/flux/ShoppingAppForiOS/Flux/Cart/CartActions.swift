import Foundation

enum CartActions: Action {
    case addItem(item: Item)
    case removeItem(item: Item)
}
