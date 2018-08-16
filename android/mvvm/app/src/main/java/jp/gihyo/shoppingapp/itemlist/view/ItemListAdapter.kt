package jp.gihyo.shoppingapp.itemlist.view

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import jp.gihyo.shoppingapp.model.Item
import timber.log.Timber
import kotlin.math.max

class ItemListAdapter(private val addItem: (Item) -> Unit): RecyclerView.Adapter<ItemListViewHolder>() {

    private val items: MutableList<Item> = mutableListOf()

    fun setItems(items: List<Item>) {
        val previousSize = itemCount
        this.items.clear()
        this.items.addAll(items)

        Timber.e("got items $items")

        notifyItemRangeChanged(0, max(previousSize, itemCount))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemListViewHolder =
            ItemListViewHolder.create(parent, addItem)

    override fun getItemCount() = items.count()

    override fun onBindViewHolder(holder: ItemListViewHolder, position: Int) {
        holder.bind(items[position])
    }

}
