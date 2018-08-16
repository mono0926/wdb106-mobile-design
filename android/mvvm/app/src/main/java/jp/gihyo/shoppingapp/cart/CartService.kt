package jp.gihyo.shoppingapp.cart

import io.reactivex.Completable
import io.reactivex.Single
import jp.gihyo.shoppingapp.api.ShoppingApi
import jp.gihyo.shoppingapp.model.CartItem
import jp.gihyo.shoppingapp.model.Item
import javax.inject.Inject

class CartService @Inject constructor(
        private val api: ShoppingApi,
        private val repository: CartRepository
) {

    fun observeItems() = repository.observeItems()

    fun addItem(item: Item): Completable = Single.just(repository.getItems())
            .map { items ->
                val mutableItems = items.toMutableList()
                val cartItem = mutableItems.find { it.item == item }

                if (cartItem != null) {
                    val index = items.indexOf(cartItem)
                    mutableItems[index] = cartItem.copy(count = cartItem.count + 1)
                } else {
                    mutableItems.add(CartItem(item, 1))
                }

                mutableItems
            }.flatMapCompletable(repository::setItems)

    fun removeItem(item: Item): Completable = Single.just(repository.getItems())
            .map { items ->
                val mutableItems = items.toMutableList()
                val cartItem = mutableItems.find { it.item == item }

                cartItem?.let {
                    val index = items.indexOf(cartItem)
                    if (cartItem.count > 1) {
                        mutableItems[index] = cartItem.copy(count = cartItem.count - 1)
                    } else {
                        mutableItems.remove(cartItem)
                    }
                }
                mutableItems
            }
            .flatMapCompletable(repository::setItems)
}