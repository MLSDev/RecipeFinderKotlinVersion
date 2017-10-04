package com.mlsdev.recipefinder.kotlinversion.di.module

import com.mlsdev.recipefinder.kotlinversion.view.analysenutrition.AnalyseNutritionFragment
import com.mlsdev.recipefinder.kotlinversion.view.analysenutrition.ingredient.IngredientAnalysisFragment
import com.mlsdev.recipefinder.kotlinversion.view.analysenutrition.recipe.RecipeAnalysisFragment
import com.mlsdev.recipefinder.kotlinversion.view.favoriterecipes.FavoriteRecipesFragment
import com.mlsdev.recipefinder.kotlinversion.view.recipedetails.RecipeDetailsFragment
import com.mlsdev.recipefinder.kotlinversion.view.searchrecipes.SearchRecipeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilderModule {

    @ContributesAndroidInjector
    abstract fun contributeSearchRecipeFragment(): SearchRecipeFragment

    @ContributesAndroidInjector
    abstract fun contributeFavoriteRecipesFragment(): FavoriteRecipesFragment

    @ContributesAndroidInjector
    abstract fun contributeIngredientAnalysisFragment(): IngredientAnalysisFragment

    @ContributesAndroidInjector
    abstract fun contributeRecipeAnalysisFragment(): RecipeAnalysisFragment

    @ContributesAndroidInjector
    abstract fun contribureRecipeDetailsFragment(): RecipeDetailsFragment

    @ContributesAndroidInjector
    abstract fun contribureAnalyseNutritionFragment(): AnalyseNutritionFragment
}