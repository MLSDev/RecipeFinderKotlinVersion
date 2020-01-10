package com.mlsdev.recipefinder.kotlinversion.di.module

import android.app.Application
import androidx.room.Room
import com.mlsdev.recipefinder.kotlinversion.data.source.local.roomdb.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application.applicationContext, AppDatabase::class.java, "recipes.db")
                .build()
    }

}
