package com.mlsdev.recipefinder.kotlinversion.view.searchrecipes

import android.databinding.ObservableField
import com.mlsdev.recipefinder.kotlinversion.data.entity.recipe.Recipe

class RecipeListItemViewModel(
        private var recipe: Recipe
) {
    val recipeTitle = ObservableField<String>(recipe.label)
    val recipeImageUrl = ObservableField<String>(recipe.image)

    fun setRecipe(recipe: Recipe) {
        this.recipe = recipe
        recipeTitle.set(recipe.label)
        recipeImageUrl.set(recipe.image)
    }

    fun getRecipe(): Recipe = recipe
}
