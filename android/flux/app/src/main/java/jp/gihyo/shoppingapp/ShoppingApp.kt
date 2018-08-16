package jp.gihyo.shoppingapp

import android.app.Application
import jp.gihyo.shoppingapp.di.AppComponent
import jp.gihyo.shoppingapp.di.DaggerAppComponent
import timber.log.Timber

class ShoppingApp: Application() {

    val component: AppComponent = DaggerAppComponent.create()

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }
}