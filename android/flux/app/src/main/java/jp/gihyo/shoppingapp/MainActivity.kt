package jp.gihyo.shoppingapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import jp.gihyo.shoppingapp.cart.CartStore
import jp.gihyo.shoppingapp.itemlist.ItemListActionCreator
import jp.gihyo.shoppingapp.itemlist.ItemListStore
import jp.gihyo.shoppingapp.itemlist.view.ItemListFragment

import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var cartStore: CartStore
    @Inject
    lateinit var itemStore: ItemListStore

    @Inject
    lateinit var itemListActionCreator: ItemListActionCreator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        (application as ShoppingApp).component.inject(this)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.content, ItemListFragment())
                    .addToBackStack(null)
                    .commit()

            // fetch data from API
            itemListActionCreator.fetchItems()
        }
    }
}
