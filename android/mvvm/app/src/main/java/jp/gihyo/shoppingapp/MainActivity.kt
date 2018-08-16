package jp.gihyo.shoppingapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import jp.gihyo.shoppingapp.itemlist.view.ItemListFragment

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.content, ItemListFragment())
                    .addToBackStack(null)
                    .commit()
        }
    }
}
