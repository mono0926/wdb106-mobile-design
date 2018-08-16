package jp.gihyo.shoppingapp.itemlist.view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import jp.gihyo.shoppingapp.R
import jp.gihyo.shoppingapp.model.Item
import kotlinx.android.synthetic.main.view_shopping_item.view.*

class ItemListViewHolder(
        val view: View,
        val addItem: (Item) -> Unit
): RecyclerView.ViewHolder(view) {

    private val context: Context
        get() = view.context

    fun bind(item: Item) {
        view.title.text = item.name
        view.price.text = context.getString(R.string.price, item.price)
        view.inventory.text = context.getString(R.string.inventory, item.inventory)

        Picasso.get().load(item.imageUrl).into(view.image)

        view.add.setOnClickListener{ addItem(item) }

        view.add.isEnabled = item.inventory > 0
    }

    companion object {
        fun create(parent: ViewGroup, addItem: (Item) -> Unit) = ItemListViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.view_shopping_item, parent, false),
                addItem
        )
    }
}