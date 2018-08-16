package jp.gihyo.shoppingapp.cart

import jp.gihyo.shoppingapp.flux.Action
import jp.gihyo.shoppingapp.model.Item

sealed class CartAction : Action()

data class AddToCart(val item: Item): CartAction()
data class RemoveFromCart(val item: Item): CartAction()