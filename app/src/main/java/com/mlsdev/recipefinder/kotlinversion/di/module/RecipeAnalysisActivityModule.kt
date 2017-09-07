package com.mlsdev.recipefinder.kotlinversion.di.module

import com.mlsdev.recipefinder.kotlinversion.view.analysenutrition.recipe.RecipeAnalysisDetailsActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class RecipeAnalysisActivityModule {
    @ContributesAndroidInjector(modules = arrayOf(FragmentBuilderModule::class))
    abstract fun contributeActivity(): RecipeAnalysisDetailsActivity
}
