package com.mlsdev.recipefinder.kotlinversion.data.source

import com.mlsdev.recipefinder.kotlinversion.data.entity.nutrition.NutritionAnalysisResult
import com.mlsdev.recipefinder.kotlinversion.data.entity.nutrition.RecipeAnalysisParams
import com.mlsdev.recipefinder.kotlinversion.data.entity.recipe.Recipe
import com.mlsdev.recipefinder.kotlinversion.data.entity.recipe.SearchResult
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

abstract class BaseDataSource : DataSource {
    override fun searchRecipes(params: Map<String, String>): Single<SearchResult> {
        return Single.just(null)
    }

    override fun getIngredientData(params: Map<String, String>): Single<NutritionAnalysisResult> {
        return Single.just(null)
    }

    override fun getFavorites(): Flowable<List<Recipe>> {
        return Flowable.just(null)
    }

    override fun addToFavorites(recipe: Recipe): Completable {
        return Completable.complete()
    }

    override fun removeFromFavorites(recipe: Recipe): Completable {
        return Completable.complete()
    }

    override fun isInFavorites(recipe: Recipe): Single<Boolean> {
        return Single.just(null)
    }

    override fun getRecipeAnalysingResult(params: RecipeAnalysisParams): Single<NutritionAnalysisResult> {
        return Single.just(null)
    }

}
