package jp.gihyo.shoppingapp.flux

import io.reactivex.Flowable
import io.reactivex.processors.PublishProcessor

class Dispatcher<Action> {

    private val dispatcher: PublishProcessor<Action> = PublishProcessor.create()

    fun dispatch(action: Action) {
        dispatcher.onNext(action)
    }

    fun observeDispatch(): Flowable<Action> = dispatcher.hide()
}