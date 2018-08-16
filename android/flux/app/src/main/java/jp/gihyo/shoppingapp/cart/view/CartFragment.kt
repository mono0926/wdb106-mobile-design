package jp.gihyo.shoppingapp.cart.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import jp.gihyo.shoppingapp.R
import jp.gihyo.shoppingapp.ShoppingApp
import jp.gihyo.shoppingapp.cart.CartActionCreator
import jp.gihyo.shoppingapp.cart.CartStore
import jp.gihyo.shoppingapp.itemlist.ItemListActionCreator
import jp.gihyo.shoppingapp.model.Item
import kotlinx.android.synthetic.main.fragment_cart.*
import timber.log.Timber
import javax.inject.Inject

class CartFragment: Fragment() {

    @Inject
    lateinit var cartActionCreator: CartActionCreator
    @Inject
    lateinit var itemListActionCreator: ItemListActionCreator

    @Inject
    lateinit var store: CartStore

    private val adapter by lazy {
        CartAdapter(::removeItem)
    }

    private val disposable = CompositeDisposable()

    private fun inject() {
        (context?.applicationContext as ShoppingApp)
                .component
                .cartComponent()
                .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_cart, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inject()

        recycler_view.adapter = adapter
        recycler_view.addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))


        disposable.addAll(
                store.observeItems()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeBy(
                                onNext = { adapter.setItems(it) },
                                onError = Timber::e
                        ),
                store.observeTotalPrice()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeBy(
                                onNext = { total_cost.text = getString(R.string.total_cost, it) },
                                onError = Timber::e
                        ),
                store.observePopBackStack()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeBy (
                            onNext = { fragmentManager?.popBackStack() },
                            onError = Timber::e
                        )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }

    private fun removeItem(item: Item) {
        cartActionCreator.removeFromCart(item)
        itemListActionCreator.removedFromCart(item)
    }


}