package jp.gihyo.shoppingapp.itemlist

import io.reactivex.Completable
import jp.gihyo.shoppingapp.api.ShoppingApi
import javax.inject.Inject

class ItemListService @Inject constructor(
        private val api: ShoppingApi,
        private val repository: ItemListRepository
) {

    fun fetchItems(): Completable = api.getItems()
            .flatMapCompletable(repository::setItems)

    fun observeItems() = repository.observeItems()

}