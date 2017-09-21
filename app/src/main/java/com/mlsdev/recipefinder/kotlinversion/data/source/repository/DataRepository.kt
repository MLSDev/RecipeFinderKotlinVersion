package com.mlsdev.recipefinder.kotlinversion.data.source.repository

import com.mlsdev.recipefinder.kotlinversion.data.entity.nutrition.NutritionAnalysisResult
import com.mlsdev.recipefinder.kotlinversion.data.entity.nutrition.RecipeAnalysisParams
import com.mlsdev.recipefinder.kotlinversion.data.entity.recipe.Recipe
import com.mlsdev.recipefinder.kotlinversion.data.source.DataSource
import com.mlsdev.recipefinder.kotlinversion.data.source.remote.ParameterKeys
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.SingleSource

class DataRepository(local: DataSource, remote: DataSource) {
    private val offset = 10
    private var from = 0
    private var more = true
    private var localDataSource: DataSource = local
    private var remoteDataSource: DataSource = remote
    private var cachedRecipes: MutableList<Recipe> = ArrayList()
    var cacheIsDirty = false

    fun searchRecipes(params: MutableMap<String, String>): Single<List<Recipe>> {
        if (!cacheIsDirty)
            return Single.just(cachedRecipes)

        from = 0
        more = true
        cachedRecipes = ArrayList()

        params[ParameterKeys.FROM] = from.toString()
        params[ParameterKeys.TO] = offset.toString()

        return getRecipes(params)
    }

    fun loadMore(params: MutableMap<String, String>): Single<List<Recipe>> {

        if (!more)
            return Single.amb(ArrayList<SingleSource<List<Recipe>>>())

        params[ParameterKeys.FROM] = from.toString()
        params[ParameterKeys.TO] = (from + offset).toString()
        return getRecipes(params)
    }

    private fun getRecipes(params: MutableMap<String, String>): Single<List<Recipe>> {
        return remoteDataSource.searchRecipes(params)
                .map { t ->
                    val recipes: MutableList<Recipe> = ArrayList()

                    t.hits.filter { !cachedRecipes.any { recipe -> recipe.uri == it.recipe.uri } }
                            .mapTo(recipes) { it.recipe }

                    cachedRecipes.addAll(recipes)
                    from += offset - (t.hits.size - recipes.size)
                    recipes as List<Recipe>
                }
                .doOnSuccess {
                    cacheIsDirty = false
                }

    }

    fun getFavoriteRecipes(): Flowable<List<Recipe>> {
        return localDataSource.getFavorites()
    }

    fun addToFavorites(recipe: Recipe): Completable {
        return localDataSource.addToFavorites(recipe)
    }

    fun removeFromFavorites(recipe: Recipe): Completable {
        return localDataSource.removeFromFavorites(recipe)
    }

    fun isInFavorites(recipe: Recipe): Single<Boolean> {
        return localDataSource.isInFavorites(recipe)
    }

    fun getIngredientData(params: Map<String, String>): Single<NutritionAnalysisResult> {
        return remoteDataSource.getIngredientData(params)
    }

    fun getRecipeAnalysisData(params: RecipeAnalysisParams): Single<NutritionAnalysisResult> {
        return remoteDataSource.getRecipeAnalysingResult(params)
    }

    init {
        cachedRecipes = ArrayList()
    }

}