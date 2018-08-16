package jp.gihyo.shoppingapp.di

import dagger.Subcomponent
import jp.gihyo.shoppingapp.cart.view.CartFragment

@ActivityScope
@Subcomponent(modules = [ApiModule::class])
interface CartComponent {

    fun inject(fragment: CartFragment)
}