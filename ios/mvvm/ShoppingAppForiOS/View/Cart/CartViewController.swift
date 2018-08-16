import UIKit
import RxSwift

final class CartViewController: UITableViewController, Injectable {
    typealias Dependency = CartViewModel

    private let closeButton = UIBarButtonItem()
    private let headerView = CartHeaderView.make()
    private let viewModel: CartViewModel
    private var dataSource = [CartItem]()
    private let disposeBag = DisposeBag()

    // MARK: Injectable

    init(with dependency: Dependency) {
        viewModel = dependency
        super.init(nibName: nil, bundle: nil)
    }

    @available(*, unavailable)
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        setup()
        bind()
    }

    private func setup() {
        navigationItem.leftBarButtonItem = closeButton
        tableView.register(CartCell.nib, forCellReuseIdentifier: CartCell.reuseIdentifier)
        tableView.tableHeaderView = headerView
        tableView.tableFooterView = UIView()
    }

    private func bind() {
        closeButton.rx.tap
            .bind(to: viewModel.closeButtonDidTap)
            .disposed(by: disposeBag)

        viewModel.title
            .bind(to: rx.title)
            .disposed(by: disposeBag)

        viewModel.closeButtonTitle
            .bind(to: closeButton.rx.title)
            .disposed(by: disposeBag)

        rx.sentMessage(#selector(viewWillAppear(_:)))
            .take(1)
            .bind { [weak self] _ in
                self?.headerView.frame.size.height = 50
            }
            .disposed(by: disposeBag)

        viewModel.dismiss
            .bind { [weak self] in
                self?.dismiss(animated: true)
            }
            .disposed(by: disposeBag)
    }
}

// MARK: TableViewDataSource

extension CartViewController {
    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return dataSource.count
    }

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cartItem = dataSource[indexPath.row]
        let cell = tableView.dequeueReusableCell(withIdentifier: CartCell.reuseIdentifier) as! CartCell
        cell.configure(cartItem)
        return cell
    }
}

// MARK: Factory method

extension CartViewController {
    static func make() -> CartViewController {
        let viewModel = CartViewModel()
        let viewControler = CartViewController(with: viewModel)
        return viewControler
    }
}
