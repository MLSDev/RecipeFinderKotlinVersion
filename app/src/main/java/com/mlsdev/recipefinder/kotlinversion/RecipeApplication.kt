package com.mlsdev.recipefinder.kotlinversion

import com.mlsdev.recipefinder.kotlinversion.di.DaggerMainComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

open class RecipeApplication : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerMainComponent.builder().application(this).build()
    }
}
