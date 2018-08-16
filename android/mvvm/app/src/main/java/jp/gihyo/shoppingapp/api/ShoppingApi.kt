package jp.gihyo.shoppingapp.api

import io.reactivex.Single
import jp.gihyo.shoppingapp.model.Item
import retrofit2.http.GET

interface ShoppingApi {

    @GET
    fun getItems(): Single<List<Item>>
}