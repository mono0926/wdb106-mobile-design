package jp.gihyo.shoppingapp.itemlist

import jp.gihyo.shoppingapp.model.Item

sealed class ItemListAction

class GotItemsAction(val items: List<Item>): ItemListAction()
class AddedToCart(val item: Item): ItemListAction()
class RemovedFromCart(val item: Item): ItemListAction()