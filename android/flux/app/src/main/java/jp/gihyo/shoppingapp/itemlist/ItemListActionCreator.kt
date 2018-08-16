package jp.gihyo.shoppingapp.itemlist

import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import jp.gihyo.shoppingapp.api.ShoppingApi
import jp.gihyo.shoppingapp.flux.ActionCreator
import jp.gihyo.shoppingapp.flux.Dispatcher
import jp.gihyo.shoppingapp.model.Item
import javax.inject.Inject

class ItemListActionCreator @Inject constructor(
        dispatcher: Dispatcher<ItemListAction>,
        private val api: ShoppingApi
)
    : ActionCreator<ItemListAction>(dispatcher) {

    fun addedToCart(item: Item) = dispatcher.dispatch(AddedToCart(item))
    fun removedFromCart(item: Item) = dispatcher.dispatch(RemovedFromCart(item))

    fun fetchItems() {
        api.getItems()
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                        onSuccess = { dispatcher.dispatch(GotItemsAction(it)) }
                )
    }

}