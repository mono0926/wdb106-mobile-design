package jp.gihyo.shoppingapp.itemlist

import jp.gihyo.shoppingapp.cart.CartService
import jp.gihyo.shoppingapp.model.Item
import javax.inject.Inject

class ItemListViewModel @Inject constructor(
        private val service: ItemListService,
        private val cartService: CartService
) {

    fun fetchItems() = service.fetchItems()

    fun observeItems() = service.observeItems()

    fun addItemToCart(item: Item) = cartService.addItem(item)

    fun removeItemFromCart(item: Item) = cartService.removeItem(item)

}