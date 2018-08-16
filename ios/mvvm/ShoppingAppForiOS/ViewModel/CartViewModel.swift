import UIKit
import RxSwift

final class CartViewModel: Injectable {
    typealias Dependency = Void
    private let closeButtonDidTapStream = PublishSubject<Void>()
    private let dismissStream = PublishSubject<Void>()
    private let disposeBag = DisposeBag()

    init(with dependency: Dependency) {
        closeButtonDidTapStream
            .bind(to: dismissStream)
            .disposed(by: disposeBag)
    }
}

// MARK: Input

extension CartViewModel {
    var closeButtonDidTap: AnyObserver<()> {
        return closeButtonDidTapStream.asObserver()
    }
}

// MARK: Output

extension CartViewModel {
    var title: Observable<String> {
        return Observable.just("カート")
    }

    var closeButtonTitle: Observable<String> {
        return Observable.just("閉じる")
    }

    var dismiss: Observable<Void> {
        return dismissStream.asObservable()
    }
}
