package jp.gihyo.shoppingapp.di

import dagger.Module
import dagger.Provides
import io.reactivex.processors.BehaviorProcessor
import jp.gihyo.shoppingapp.cart.CartRepository
import javax.inject.Singleton

@Module
class CartModule {

    @Singleton
    @Provides
    fun provideCartRepository() = CartRepository(BehaviorProcessor.create())



}