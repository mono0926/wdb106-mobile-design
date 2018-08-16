package jp.gihyo.shoppingapp.cart

import android.arch.lifecycle.LifecycleObserver
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import jp.gihyo.shoppingapp.flux.Dispatcher
import jp.gihyo.shoppingapp.flux.Store
import jp.gihyo.shoppingapp.model.CartItem
import javax.inject.Inject

class CartStore @Inject constructor(dispatcher: Dispatcher<CartAction>)
    : Store<CartAction>(dispatcher), LifecycleObserver {

    private val cartItems: BehaviorSubject<List<CartItem>>
            = BehaviorSubject.create()

    fun itemsCount(): Int = cartItems.value?.map { it.count }?.sum() ?: 0

    fun observeItems(): Observable<List<CartItem>> = cartItems.hide()

    fun observeTotalPrice(): Observable<Int> = cartItems
            .map { items ->
                items.map { (item, count) ->
                    item.price * count
                }.reduce { left, right -> left + right  }
            }

    fun observePopBackStack(): Observable<()->Unit> = cartItems
            .map { it.isEmpty() }
            .filter { it }
            .map { {} }

    override fun handleAction(action: CartAction) {
        when (action) {
            is AddToCart -> {
                val item = action.item
                val mutableItems = cartItems.value?.toMutableList() ?: mutableListOf()
                val cartItem = mutableItems.find { it.item.id == item.id }

                if (cartItem != null) {
                    val index = mutableItems.indexOf(cartItem)
                    mutableItems[index] = cartItem.copy(count = cartItem.count + 1)
                } else {
                    mutableItems.add(CartItem(item, 1))
                }

                cartItems.onNext(mutableItems)
            }
            is RemoveFromCart -> {
                val item = action.item
                val mutableItems = cartItems.value?.toMutableList() ?: mutableListOf()
                val cartItem = mutableItems.find { it.item.id == item.id }

                cartItem?.let {
                    val index = mutableItems.indexOf(cartItem)
                    if (cartItem.count > 1) {
                        mutableItems[index] = cartItem.copy(count = cartItem.count - 1)
                    } else {
                        mutableItems.remove(cartItem)
                    }
                }

                cartItems.onNext(mutableItems)
            }
        }
    }




}