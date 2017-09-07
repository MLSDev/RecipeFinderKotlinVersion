package com.mlsdev.recipefinder.kotlinversion.di;

import android.app.Application
import com.mlsdev.recipefinder.kotlinversion.RecipeApplication
import com.mlsdev.recipefinder.kotlinversion.di.module.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        AndroidInjectionModule::class,
        AppModule::class,
        UtilsModule::class,
        DataSourceModule::class,
        DatabaseModule::class,
        ApiModule::class,
        MainActivityModule::class,
        RecipeAnalysisActivityModule::class,
        ViewModelModule::class))
interface MainComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): MainComponent
    }

    fun inject(application: RecipeApplication)

}
