package com.mlsdev.recipefinder.kotlinversion.data.source.remote


import com.mlsdev.recipefinder.kotlinversion.data.entity.nutrition.NutritionAnalysisResult
import com.mlsdev.recipefinder.kotlinversion.data.entity.nutrition.RecipeAnalysisParams
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap

interface NutritionAnalysisService {

    @GET(PathConstants.NUTRITION_DATA)
    fun analyzeIngredient(@QueryMap params: Map<String, String>): Single<NutritionAnalysisResult>

    @POST(PathConstants.NUTRITION_DETAILS)
    fun analyzeRecipe(@Body params: RecipeAnalysisParams,
                      @QueryMap credentials: Map<String, String>): Single<NutritionAnalysisResult>

}
