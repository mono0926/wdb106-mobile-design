import UIKit
import RxSwift

final class ItemListViewController: UITableViewController, Injectable {
    typealias Dependency = ItemListViewModel

    private let cartButton = UIBarButtonItem()
    private let viewModel: ItemListViewModel
    private var dataSource = [Item]()
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
        navigationItem.leftBarButtonItem = cartButton
        tableView.tableFooterView = UIView()
        tableView.register(ItemCell.nib, forCellReuseIdentifier: ItemCell.reuseIdentifier)
    }

    private func bind() {
        rx.sentMessage(#selector(viewWillAppear(_:)))
            .map { _ in }
            .bind(to: viewModel.viewWillAppear)
            .disposed(by: disposeBag)

        cartButton.rx.tap
            .bind(to: viewModel.cartButtonDidTap)
            .disposed(by: disposeBag)

        viewModel.title
            .bind(to: rx.title)
            .disposed(by: disposeBag)

        viewModel.cartButtonTitle
            .bind(to: cartButton.rx.title)
            .disposed(by: disposeBag)

        viewModel.items
            .bind { [weak self] items in
                self?.reloadData(items)
            }
            .disposed(by: disposeBag)

        viewModel.navigateToCart
            .bind { [weak self] in
                self?.navigateToCart()
            }
            .disposed(by: disposeBag)
    }

    private func reloadData(_ data: [Item]) {
        dataSource = data
        tableView.reloadData()
    }

    private func navigateToCart() {
        let viewController = CartViewController.make()
        let navigation = UINavigationController(
            rootViewController: viewController
        )
        present(navigation, animated: true)
    }
}

// MARK: TableViewDataSource

extension ItemListViewController {
    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return dataSource.count
    }

    override func tableView(_ tableView: UITableView, willDisplay cell: UITableViewCell, forRowAt indexPath: IndexPath) {
        guard let cell = cell as? ItemCell else { return }
        cell.addedItem
            .bind(to: viewModel.addButtonDidTap)
            .disposed(by: cell.disposeBag)
    }

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let item = dataSource[indexPath.row]
        let cell = tableView.dequeueReusableCell(withIdentifier: ItemCell.reuseIdentifier) as! ItemCell
        cell.configure(item)
        return cell
    }
}
