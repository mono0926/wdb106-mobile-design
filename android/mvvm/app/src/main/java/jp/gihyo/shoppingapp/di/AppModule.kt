package jp.gihyo.shoppingapp.di

import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @Provides
    fun provideCartModule() = CartModule()
}