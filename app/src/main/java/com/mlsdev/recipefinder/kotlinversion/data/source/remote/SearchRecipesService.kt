package com.mlsdev.recipefinder.kotlinversion.data.source.remote

import com.mlsdev.recipefinder.kotlinversion.data.entity.recipe.SearchResult


import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface SearchRecipesService {

    @GET(PathConstants.SEARCH)
    fun searchRecipes(@QueryMap params: Map<String, String>): Single<SearchResult>

}
