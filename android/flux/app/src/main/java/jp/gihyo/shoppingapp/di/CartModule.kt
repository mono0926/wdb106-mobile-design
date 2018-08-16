package jp.gihyo.shoppingapp.di

import dagger.Module
import dagger.Provides
import jp.gihyo.shoppingapp.cart.CartAction
import jp.gihyo.shoppingapp.cart.CartActionCreator
import jp.gihyo.shoppingapp.cart.CartStore
import jp.gihyo.shoppingapp.flux.Dispatcher
import javax.inject.Singleton

@Module
class CartModule {

    @Singleton
    @Provides
    fun provideCartStore(dispatcher: Dispatcher<CartAction>) = CartStore(dispatcher)

    @Singleton
    @Provides
    fun provideCartDispatcher() = Dispatcher<CartAction>()

    @Provides
    fun provideCartActionCreator(dispatcher: Dispatcher<CartAction>) = CartActionCreator(dispatcher)

}