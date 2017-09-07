package com.mlsdev.recipefinder.kotlinversion.di.module

import com.google.gson.Gson
import com.mlsdev.recipefinder.kotlinversion.BuildConfig
import com.mlsdev.recipefinder.kotlinversion.data.source.remote.NutritionAnalysisService
import com.mlsdev.recipefinder.kotlinversion.data.source.remote.PathConstants
import com.mlsdev.recipefinder.kotlinversion.data.source.remote.SearchRecipesService
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import okhttp3.logging.HttpLoggingInterceptor.Level.NONE
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class ApiModule {

    companion object {
        const val HTTP_LOGGING = "http_logging_interceptor"
        const val baseUrl = PathConstants.BASE_URL
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Named(HTTP_LOGGING)
    fun provideHttpLoggingInterceptor(): Interceptor {
        val interceptor = HttpLoggingInterceptor()
        val level: Level = if (BuildConfig.DEBUG) BODY else NONE
        interceptor.level = level
        return interceptor
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(@Named(HTTP_LOGGING) httpLoggingInterceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    @Provides
    @Singleton
    fun provideSearchRecipesService(retrofit: Retrofit): SearchRecipesService {
        return retrofit.create(SearchRecipesService::class.java)
    }

    @Provides
    @Singleton
    fun provideNutritionAnalysisService(retrofit: Retrofit): NutritionAnalysisService {
        return retrofit.create(NutritionAnalysisService::class.java)
    }

}
