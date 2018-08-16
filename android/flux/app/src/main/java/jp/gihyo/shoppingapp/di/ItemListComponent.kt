package jp.gihyo.shoppingapp.di

import dagger.Subcomponent
import jp.gihyo.shoppingapp.itemlist.view.ItemListFragment

@ActivityScope
@Subcomponent(modules = [ApiModule::class])
interface ItemListComponent {

    fun inject(fragment: ItemListFragment)
}
