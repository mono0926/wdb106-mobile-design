package jp.gihyo.shoppingapp.model

data class Item (
        val id: Int,
        val name: String,
        val price: Int,
        val imageUrl: String,
        val description: String,
        val inventory: Int
)