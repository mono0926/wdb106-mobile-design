package jp.gihyo.shoppingapp.cart.view

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import jp.gihyo.shoppingapp.model.CartItem
import jp.gihyo.shoppingapp.model.Item
import timber.log.Timber
import kotlin.math.max

class CartAdapter(private val removeItem: (Item) -> Unit) : RecyclerView.Adapter<CartItemViewHolder>() {

    private val items: MutableList<CartItem> = mutableListOf()

    fun setItems(items: List<CartItem>) {
        val previousSize = itemCount
        this.items.clear()
        this.items.addAll(items)

        Timber.e("got items $items")

        notifyItemRangeChanged(0, max(previousSize, itemCount))
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            CartItemViewHolder.create(parent, removeItem)

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        holder.bind(items[position])
    }
}