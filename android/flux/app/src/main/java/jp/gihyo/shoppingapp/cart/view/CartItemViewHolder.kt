package jp.gihyo.shoppingapp.cart.view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import jp.gihyo.shoppingapp.R
import jp.gihyo.shoppingapp.model.CartItem
import jp.gihyo.shoppingapp.model.Item
import kotlinx.android.synthetic.main.view_cart_item.view.*

class CartItemViewHolder(
        val view: View,
        val removeItem: (Item) -> Unit
) : RecyclerView.ViewHolder(view){

    private val context: Context
        get() = view.context

    fun bind(cartItem: CartItem) {
        val (item, count) = cartItem
        view.title.text = item.name
        view.price.text = context.getString(R.string.price, item.price)
        view.count.text = context.getString(R.string.quantity, count)

        Picasso.get().load(item.imageUrl).into(view.image)

        view.remove.setOnClickListener{ removeItem(item) }
    }

    companion object {
        fun create(parent: ViewGroup, removeItem: (Item) -> Unit) = CartItemViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.view_cart_item, parent, false),
                removeItem
        )
    }
}