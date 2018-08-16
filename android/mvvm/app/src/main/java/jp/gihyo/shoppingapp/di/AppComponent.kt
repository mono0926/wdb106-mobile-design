package jp.gihyo.shoppingapp.di

import dagger.Component
import jp.gihyo.shoppingapp.cart.CartRepository
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, CartModule::class])
interface AppComponent {

    fun plus(module: ItemListModule): ItemListComponent

    fun cartComponent(): CartComponent

    fun getCartRepository(): CartRepository


}