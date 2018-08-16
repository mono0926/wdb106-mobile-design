package jp.gihyo.shoppingapp.itemlist.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.view.*
import android.widget.LinearLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import jp.gihyo.shoppingapp.R
import jp.gihyo.shoppingapp.ShoppingApp
import jp.gihyo.shoppingapp.cart.view.CartFragment
import jp.gihyo.shoppingapp.di.ItemListModule
import jp.gihyo.shoppingapp.itemlist.ItemListViewModel
import jp.gihyo.shoppingapp.model.Item
import kotlinx.android.synthetic.main.fragment_item_list.*
import timber.log.Timber
import javax.inject.Inject


class ItemListFragment: Fragment() {

    @Inject
    lateinit var viewModel: ItemListViewModel

    private val adapter by lazy {
        ItemListAdapter(::addItem)
    }

    private val disposable = CompositeDisposable()

    private fun inject() {
        (context?.applicationContext as ShoppingApp)
                .component
                .plus(ItemListModule())
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
                viewModel.fetchItems()
                        .subscribe(),
                viewModel.observeItems()
                        .observeOn(AndroidSchedulers.mainThread())
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
        disposable.add(viewModel.addItemToCart(item).subscribe())
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater?.inflate(R.menu.menu_main, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_cart -> {
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