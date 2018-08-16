package jp.gihyo.shoppingapp.cart

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.processors.BehaviorProcessor
import jp.gihyo.shoppingapp.model.CartItem
import timber.log.Timber
import javax.inject.Inject

class CartRepository @Inject constructor(
        private val items: BehaviorProcessor<List<CartItem>>
) {

    init {
        Timber.e("this: $this")
    }

    fun observeItems(): Flowable<List<CartItem>> = items.hide()

    fun setItems(items: List<CartItem>): Completable = Completable.fromAction {
        this.items.onNext(items)
    }

    fun getItems(): List<CartItem> = items.value ?: emptyList()
}