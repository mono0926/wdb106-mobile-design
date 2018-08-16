package jp.gihyo.shoppingapp.cart.view

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import jp.gihyo.shoppingapp.BR
import jp.gihyo.shoppingapp.R
import jp.gihyo.shoppingapp.model.CartItem

class CartItemViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root){

    fun bind(cartItem: CartItem) {
        binding.setVariable(BR.item, cartItem)
    }

    companion object {
        fun create(parent: ViewGroup) = CartItemViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.view_cart_item,
                parent,
                false
            )
        )
    }
}