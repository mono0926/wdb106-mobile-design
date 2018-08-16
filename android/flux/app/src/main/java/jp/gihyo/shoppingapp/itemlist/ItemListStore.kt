package jp.gihyo.shoppingapp.itemlist

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import jp.gihyo.shoppingapp.flux.Dispatcher
import jp.gihyo.shoppingapp.flux.Store
import jp.gihyo.shoppingapp.model.Item
import javax.inject.Inject

class ItemListStore @Inject constructor(dispatcher: Dispatcher<ItemListAction>)
    : Store<ItemListAction>(dispatcher) {

    private val items: BehaviorSubject<List<Item>>
            = BehaviorSubject.create()

    fun observeItems(): Observable<List<Item>> = items.hide()

    override fun handleAction(action: ItemListAction) {
        when (action) {
            is GotItemsAction -> {
                items.onNext(action.items)
            }

            is AddedToCart -> {
                val mutableItems = items.value?.toMutableList() ?: mutableListOf()
                val item = mutableItems.find { it.id == action.item.id }

                if (item != null) {
                    val index = mutableItems.indexOf(item)
                    mutableItems[index] = item.copy(inventory = if (item.inventory > 0) item.inventory - 1 else 0)
                    items.onNext(mutableItems)
                }
            }
            is RemovedFromCart -> {
                val mutableItems = items.value?.toMutableList() ?: mutableListOf()
                val item = mutableItems.find { it.id == action.item.id }

                if (item != null) {
                    val index = mutableItems.indexOf(item)
                    mutableItems[index] = item.copy(inventory = item.inventory + 1)
                    items.onNext(mutableItems)
                }
            }
        }
    }

}