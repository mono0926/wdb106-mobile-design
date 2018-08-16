package jp.gihyo.shoppingapp.di

import dagger.Module
import dagger.Provides
import jp.gihyo.shoppingapp.api.ShoppingApi
import jp.gihyo.shoppingapp.flux.Dispatcher
import jp.gihyo.shoppingapp.itemlist.ItemListAction
import jp.gihyo.shoppingapp.itemlist.ItemListActionCreator
import jp.gihyo.shoppingapp.itemlist.ItemListStore
import javax.inject.Singleton

@Module
class ItemListModule {

    @Provides
    fun provideItemListActionCreator(
            dispatcher: Dispatcher<ItemListAction>,
            api: ShoppingApi
    ) = ItemListActionCreator(dispatcher, api)

    @Singleton
    @Provides
    fun provideItemListDispatcher() = Dispatcher<ItemListAction>()

    @Singleton
    @Provides
    fun provideItemListStore(dispatcher: Dispatcher<ItemListAction>)
            = ItemListStore(dispatcher)

}