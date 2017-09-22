package com.mlsdev.recipefinder.kotlinversion.data.source.remote

import com.mlsdev.recipefinder.kotlinversion.BuildConfig
import com.mlsdev.recipefinder.kotlinversion.data.entity.nutrition.NutritionAnalysisResult
import com.mlsdev.recipefinder.kotlinversion.data.entity.nutrition.RecipeAnalysisParams
import com.mlsdev.recipefinder.kotlinversion.data.entity.recipe.SearchResult
import com.mlsdev.recipefinder.kotlinversion.data.source.BaseDataSource
import com.mlsdev.recipefinder.kotlinversion.data.source.DataSource
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

open class RemoteDataSource : BaseDataSource, DataSource {

    private lateinit var searchRecipesService: SearchRecipesService
    private lateinit var nutritionAnalysisService: NutritionAnalysisService

    companion object {
        var instance: RemoteDataSource? = null
        var url = PathConstants.BASE_URL

        fun getInsatnce(): RemoteDataSource {
            if (instance == null)
                instance = RemoteDataSource()

            return instance as RemoteDataSource
        }

        fun setBaseUrl(url: String) {
            this.url = url
            instance = null
        }
    }

    constructor() {
        initApiServices()
    }

    private fun initApiServices() {
        val interceptor = HttpLoggingInterceptor()
        val level: HttpLoggingInterceptor.Level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        interceptor.level = level

        val client: OkHttpClient = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()

        val retrofit: Retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        searchRecipesService = retrofit.create(SearchRecipesService::class.java)
        nutritionAnalysisService = retrofit.create(NutritionAnalysisService::class.java)
    }

    override fun searchRecipes(params: Map<String, String>): Single<SearchResult> {
        setCredentials(params as MutableMap<String, String>, true)
        return searchRecipesService.searchRecipes(params)
    }

    override fun getIngredientData(params: Map<String, String>): Single<NutritionAnalysisResult> {
        setCredentials(params as MutableMap<String, String>, false)
        return nutritionAnalysisService.analyzeIngredient(params)
    }

    override fun getRecipeAnalysingResult(params: RecipeAnalysisParams): Single<NutritionAnalysisResult> {
        val credentials: MutableMap<String, String> = HashMap()
        setCredentials(credentials, false)
        return nutritionAnalysisService.analyzeRecipe(params, credentials)
    }

    private fun setCredentials(params: MutableMap<String, String>, search: Boolean) {
        val appId = if (search) BuildConfig.SEARCH_APP_ID else BuildConfig.ANALYSE_APP_ID
        val appKey = if (search) BuildConfig.SEARCH_APP_KEY else BuildConfig.ANALYSE_APP_KEY
        params[ParameterKeys.APP_ID] = appId
        params[ParameterKeys.APP_KEY] = appKey
    }
}
