package jp.gihyo.shoppingapp.itemlist.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
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
import jp.gihyo.shoppingapp.cart.view.CartFragment
import jp.gihyo.shoppingapp.itemlist.ItemListActionCreator
import jp.gihyo.shoppingapp.itemlist.ItemListStore
import jp.gihyo.shoppingapp.model.Item
import kotlinx.android.synthetic.main.fragment_item_list.*
import timber.log.Timber
import javax.inject.Inject


class ItemListFragment: Fragment() {

    @Inject
    lateinit var cartActionCreator: CartActionCreator

    @Inject
    lateinit var itemListActionCreator: ItemListActionCreator

    @Inject
    lateinit var itemListStore: ItemListStore

    @Inject
    lateinit var cartStore: CartStore

    private val adapter by lazy {
        ItemListAdapter(::addItem)
    }

    private var menu: Menu? = null
    private val disposable = CompositeDisposable()

    private fun inject() {
        (context?.applicationContext as ShoppingApp)
                .component
                .itemListComponent()
                .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_item_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inject()
        recycler_view.adapter = adapter
        recycler_view.addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))

        setHasOptionsMenu(true)

        disposable.addAll(
                itemListStore.observeItems()
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext({
                                val cartMenu = this.menu?.findItem(R.id.action_cart)
                                cartMenu?.title = getString(R.string.cart_count, cartStore.itemsCount())
                        })
                        .subscribeBy(
                                onNext = adapter::setItems,
                                onError = Timber::e
                        )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }

    private fun addItem(item: Item) {
        cartActionCreator.addToCart(item)
        itemListActionCreator.addedToCart(item)

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater?.inflate(R.menu.menu_main, menu)
        this.menu = menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_cart -> {

                if (cartStore.itemsCount() == 0) {
                    return true
                }

                activity?.run {
                    supportFragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right, R.anim.slide_in_left, R.anim.slide_out_left)
                            .replace(R.id.content, CartFragment())
                            .addToBackStack(null)
                            .commit()
                }
                    true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}