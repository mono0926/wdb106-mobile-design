package jp.gihyo.shoppingapp.flux

import android.util.TimeUtils
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber
import java.util.concurrent.TimeUnit

abstract class Store<Action : Any>(val dispatcher: Dispatcher<Action>) {

    init {
        dispatcher
                .observeDispatch()
                .subscribeBy(
                        onNext = ::handleAction,
                        onError = Timber::e
                )
    }

    abstract fun handleAction(action: Action)
}