package jp.gihyo.shoppingapp.itemlist.view

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import jp.gihyo.shoppingapp.BR
import jp.gihyo.shoppingapp.R
import jp.gihyo.shoppingapp.model.Item

class ItemListViewHolder(
    private val binding: ViewDataBinding,
    val addItem: (Item) -> Unit
): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Item) {
        binding.setVariable(BR.item, item)
        binding.setVariable(BR.viewHolder, this)
    }

    companion object {
        fun create(parent: ViewGroup, addItem: (Item) -> Unit) = ItemListViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.view_shopping_item, parent,
                false
            ),
            addItem
        )
    }
}