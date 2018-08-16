import Foundation
import RxSwift
import RxCocoa

final class ItemStore: Store {

    private let disposeBag = DisposeBag()
    private let itemsStream = BehaviorRelay<[Item]>(value: [])
    private let isRequestingStream = BehaviorRelay<Bool>(value: false)
    private let errorStream = PublishRelay<Error>()

    init(with dispatcher: Dispatcher = .shared) {
        
        /// Handle ItemActions
        dispatcher.register(handler: { [weak self] (action: ItemActions) in
            guard let `self` = self else { return }
            
            switch action {
            case .receiveItems(let items):
                self.handleReceive(items)
            case .receiveError(let error):
                self.handleError(error)
            case .requestItems:
                self.handleRequest()
            }
            
        }).disposed(by: disposeBag)
        
        // Handle CartActions
        dispatcher.register(handler: { [weak self] (action: CartActions) in
            guard let `self` = self else { return }
            
            switch action {
            case .addItem(let item):
                self.decrease(item)
            case .removeItem(let item):
                self.increase(item)
            }
            
        }).disposed(by: disposeBag)
        
    }

    // MARK: Output
    
    var items: Observable<[Item]> {
        return itemsStream.asObservable()
    }

    var isRequesting: Observable<Bool> {
        return isRequestingStream.asObservable()
    }

    var error: Observable<Error> {
        return errorStream.asObservable()
    }

    // MARK: Mutation
    
    private func handleRequest() {
        isRequestingStream.accept(false)
    }
    
    private func handleError(_ error: Error) {
        errorStream.accept(error)
    }
    
    private func handleReceive(_ items: [Item]) {
        itemsStream.accept(items)
    }
    
    private func increase(_ item: Item) {
        update(item, with: {
            $0.increase()
            return $0
        })
    }
    
    private func decrease(_ item: Item) {
        update(item, with: {
            $0.decrease()
            return $0
        })
    }
    
    private func update(_ item: Item, with transform: (_ item: inout Item) -> Item) {
        let items = itemsStream.value.map { element -> Item in
            if element.id == item.id {
                var element = element
                return transform(&element)
            }
            return element
        }
        itemsStream.accept(items)
    }
    
}
