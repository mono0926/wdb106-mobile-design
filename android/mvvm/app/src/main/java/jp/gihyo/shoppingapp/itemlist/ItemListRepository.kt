package jp.gihyo.shoppingapp.itemlist

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.processors.BehaviorProcessor
import jp.gihyo.shoppingapp.model.Item
import javax.inject.Inject

class ItemListRepository @Inject constructor(
        private val items: BehaviorProcessor<List<Item>>
) {

    fun observeItems(): Flowable<List<Item>> = items.hide()

    fun setItems(items: List<Item>): Completable = Completable.fromAction {
        this.items.onNext(items)
    }

}