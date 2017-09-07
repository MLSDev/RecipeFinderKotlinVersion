package com.mlsdev.recipefinder.kotlinversion.di.module

import com.mlsdev.recipefinder.kotlinversion.data.source.DataSource
import com.mlsdev.recipefinder.kotlinversion.data.source.local.LocalDataSource
import com.mlsdev.recipefinder.kotlinversion.data.source.local.roomdb.AppDatabase
import com.mlsdev.recipefinder.kotlinversion.data.source.remote.RemoteDataSource
import com.mlsdev.recipefinder.kotlinversion.data.source.repository.DataRepository
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class DataSourceModule {

    companion object {
        const val LOCAL_DATA_SOURCE = "local_data_source"
        const val REMOTE_DATA_SOURCE = "remote_data_source"
    }

    @Provides
    @Singleton
    fun provideDataRepository(@Named(LOCAL_DATA_SOURCE) local: DataSource,
                              @Named(REMOTE_DATA_SOURCE) remote: DataSource): DataRepository {
        return DataRepository(local, remote)
    }

    @Provides
    @Singleton
    @Named(LOCAL_DATA_SOURCE)
    fun provideLocalDataSource(database: AppDatabase): DataSource {
        return LocalDataSource(database)
    }

    @Provides
    @Singleton
    @Named(REMOTE_DATA_SOURCE)
    fun provideRemoteDataSource(): DataSource {
        return RemoteDataSource()
    }

}
