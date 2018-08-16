package jp.gihyo.shoppingapp.di

import dagger.Module
import dagger.Provides
import io.reactivex.Single
import jp.gihyo.shoppingapp.api.ShoppingApi
import jp.gihyo.shoppingapp.model.Item


@Module
class ApiModule {

    @Provides
    fun provideShoppingApi() = object : ShoppingApi {

    override fun getItems(): Single<List<Item>> = Single.just(listOf(
           Item(0, "WEB+DB PRESS 100", 1480, "https://gihyo.jp/assets/images/gdp/2017/978-4-7741-9229-1.jpg", ""),
           Item(1, "WEB+DB PRESS 101", 1480, "https://gihyo.jp/assets/images/gdp/2017/978-4-7741-9392-2.jpg", ""),
           Item(2, "WEB+DB PRESS 102", 1480, "https://gihyo.jp/assets/images/gdp/2017/978-4-7741-9533-9.jpg", ""),
           Item(3, "WEB+DB PRESS 103", 1480, "https://gihyo.jp/assets/images/gdp/2018/978-4-7741-9668-8.jpg", "")
        ))
    }
}