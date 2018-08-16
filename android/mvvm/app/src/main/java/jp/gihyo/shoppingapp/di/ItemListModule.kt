package jp.gihyo.shoppingapp.di

import dagger.Module
import dagger.Provides
import io.reactivex.processors.BehaviorProcessor
import jp.gihyo.shoppingapp.api.ShoppingApi
import jp.gihyo.shoppingapp.itemlist.ItemListRepository
import jp.gihyo.shoppingapp.itemlist.ItemListService

@Module
class ItemListModule {

    @Provides
    @ActivityScope
    fun provideItemListRepository() = ItemListRepository(BehaviorProcessor.create())

    @Provides
    fun provideItemListService(api: ShoppingApi, repository: ItemListRepository)
            = ItemListService(api, repository)


}