package com.mlsdev.recipefinder.kotlinversion.data.repository

import com.mlsdev.recipefinder.kotlinversion.BuildConfig
import com.mlsdev.recipefinder.kotlinversion.data.entity.recipe.Recipe
import com.mlsdev.recipefinder.kotlinversion.data.source.local.LocalDataSource
import com.mlsdev.recipefinder.kotlinversion.data.source.remote.RemoteDataSource
import com.mlsdev.recipefinder.kotlinversion.data.source.repository.DataRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.robolectric.annotation.Config

@RunWith(MockitoJUnitRunner::class)
@Config(constants = BuildConfig::class, manifest = Config.NONE, sdk = intArrayOf(26))
class DataRepositoryTest {

    @Mock
    lateinit var localDataSource: LocalDataSource

    @Mock
    lateinit var remoteDataSource: RemoteDataSource

    @InjectMocks
    lateinit var repository: DataRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        `when`(localDataSource.getFavorites()).thenReturn(Flowable.just(ArrayList())!!)
        repository = DataRepository(localDataSource, remoteDataSource)
    }

    @Test
    fun testGetFavoriteRecipes() {
        repository.getFavoriteRecipes()
        verify(localDataSource, atLeastOnce()).getFavorites()
    }

    @Test
    fun testAddToFavorites() {
        val recipe = Recipe()
        `when`(localDataSource.addToFavorites(recipe)).thenReturn(Completable.complete())
        repository.addToFavorites(recipe)
        verify(localDataSource, atLeastOnce()).addToFavorites(recipe)
    }

    @Test
    fun testRemoveFromFavorites() {
        val recipe = Recipe()
        `when`(localDataSource.removeFromFavorites(recipe)).thenReturn(Completable.complete())
        repository.removeFromFavorites(recipe)
        verify(localDataSource, atLeastOnce()).removeFromFavorites(recipe)
    }

    @Test
    fun testIsInFavorites_true() {
        val recipe = Recipe()
        `when`(localDataSource.isInFavorites(recipe)).thenReturn(Single.just(true))
        repository.isInFavorites(recipe)
        verify(localDataSource, atLeastOnce()).isInFavorites(recipe)
    }

    @Test
    fun testIsInFavorites_false() {
        val recipe = Recipe()
        `when`(localDataSource.isInFavorites(recipe)).thenReturn(Single.just(false))
        repository.isInFavorites(recipe)
        verify(localDataSource, atLeastOnce()).isInFavorites(recipe)
    }

}