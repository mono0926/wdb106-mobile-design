package jp.gihyo.shoppingapp.cart

import jp.gihyo.shoppingapp.model.CartItem
import javax.inject.Inject

class CartViewModel @Inject constructor(private val service: CartService) {

    fun observeItems() = service.observeItems()

    fun observeTotalPrice() = service.observeItems()
            .map { items ->

                items.map { (item, count) ->
                    item.price * count
                }.reduce { left, right -> left + right  }
            }

    fun addItem(cartItem: CartItem) = service.addItem(cartItem.item)

    fun removeItem(cartItem: CartItem) = service.removeItem(cartItem.item)
}