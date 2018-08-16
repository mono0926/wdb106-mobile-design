import Foundation

enum ItemActions: Action {
    case requestItems
    case receiveItems(items: [Item])
    case receiveError(error: Error)
}
