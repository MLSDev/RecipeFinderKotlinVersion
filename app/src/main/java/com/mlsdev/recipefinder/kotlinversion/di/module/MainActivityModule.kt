package com.mlsdev.recipefinder.kotlinversion.di.module

import com.mlsdev.recipefinder.kotlinversion.view.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class MainActivityModule {

    @ContributesAndroidInjector(modules = arrayOf(FragmentBuilderModule::class))
    abstract fun contributeMainActivityInjector(): MainActivity
}
