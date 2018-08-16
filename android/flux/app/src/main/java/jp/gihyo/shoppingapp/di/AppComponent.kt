package jp.gihyo.shoppingapp.di

import dagger.Component
import jp.gihyo.shoppingapp.MainActivity
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, CartModule::class, ItemListModule::class, ApiModule::class])
interface AppComponent {

    fun itemListComponent(): ItemListComponent

    fun cartComponent(): CartComponent

    fun inject(activity: MainActivity)


}