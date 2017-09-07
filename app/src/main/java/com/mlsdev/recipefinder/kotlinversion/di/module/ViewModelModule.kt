package com.mlsdev.recipefinder.kotlinversion.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.mlsdev.recipefinder.kotlinversion.view.analysenutrition.ingredient.IngredientAnalysisViewModel
import com.mlsdev.recipefinder.kotlinversion.view.analysenutrition.recipe.RecipeAnalysisDetailsViewModel
import com.mlsdev.recipefinder.kotlinversion.view.analysenutrition.recipe.RecipeAnalysisViewModel
import com.mlsdev.recipefinder.kotlinversion.view.favoriterecipes.FavoritesViewModel
import com.mlsdev.recipefinder.kotlinversion.view.recipedetails.RecipeViewModel
import com.mlsdev.recipefinder.kotlinversion.view.searchrecipes.SearchViewModel
import com.mlsdev.recipefinder.kotlinversion.view.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindSearchViewModel(searchViewModel: SearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FavoritesViewModel::class)
    abstract fun bindFavoritesViewModel(favoritesViewModel: FavoritesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(IngredientAnalysisViewModel::class)
    abstract fun bindIngredientAnalysisViewModel(ingredientAnalysisViewModel: IngredientAnalysisViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RecipeAnalysisViewModel::class)
    abstract fun bindRecipeAnalysisViewModel(recipeAnalysisViewModel: RecipeAnalysisViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RecipeViewModel::class)
    abstract fun bindRecipeViewModel(recipeViewModel: RecipeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RecipeAnalysisDetailsViewModel::class)
    abstract fun bindRecipeAnalysisDetailsViewModel(viewModel: RecipeAnalysisDetailsViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

}
