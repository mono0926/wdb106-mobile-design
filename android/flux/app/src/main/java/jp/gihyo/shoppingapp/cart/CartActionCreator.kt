package jp.gihyo.shoppingapp.cart

import jp.gihyo.shoppingapp.flux.ActionCreator
import jp.gihyo.shoppingapp.flux.Dispatcher
import jp.gihyo.shoppingapp.model.Item
import javax.inject.Inject

class CartActionCreator @Inject constructor(dispatcher: Dispatcher<CartAction>)
    : ActionCreator<CartAction>(dispatcher) {

    fun addToCart(item: Item) = dispatcher.dispatch(AddToCart(item))

    fun removeFromCart(item: Item) = dispatcher.dispatch(RemoveFromCart(item))
}