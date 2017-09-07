package com.mlsdev.recipefinder.kotlinversion.data.source

import com.mlsdev.recipefinder.kotlinversion.data.entity.nutrition.NutritionAnalysisResult
import com.mlsdev.recipefinder.kotlinversion.data.entity.nutrition.RecipeAnalysisParams
import com.mlsdev.recipefinder.kotlinversion.data.entity.recipe.Recipe
import com.mlsdev.recipefinder.kotlinversion.data.entity.recipe.SearchResult
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface DataSource {
    fun searchRecipes(params: Map<String, String>): Single<SearchResult>

    fun getIngredientData(params: Map<String, String>): Single<NutritionAnalysisResult>

    fun getFavorites(): Flowable<List<Recipe>>

    fun addToFavorites(recipe: Recipe): Completable

    fun removeFromFavorites(recipe: Recipe): Completable

    fun isInFavorites(recipe: Recipe): Single<Boolean>

    fun getRecipeAnalysingResult(params: RecipeAnalysisParams): Single<NutritionAnalysisResult>
}
