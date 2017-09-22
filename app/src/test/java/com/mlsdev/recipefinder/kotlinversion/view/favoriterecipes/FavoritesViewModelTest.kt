package com.mlsdev.recipefinder.kotlinversion.view.favoriterecipes

import android.content.Context
import android.view.View
import com.mlsdev.recipefinder.kotlinversion.BuildConfig
import com.mlsdev.recipefinder.kotlinversion.data.entity.recipe.Recipe
import com.mlsdev.recipefinder.kotlinversion.data.source.repository.DataRepository
import com.mlsdev.recipefinder.kotlinversion.view.listener.OnDataLoadedListener
import io.reactivex.Flowable
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.robolectric.annotation.Config

@RunWith(MockitoJUnitRunner::class)
@Config(constants = BuildConfig::class, manifest = Config.NONE, sdk = intArrayOf(23))
class FavoritesViewModelTest {

    @Mock
    lateinit var context: Context

    @Mock
    lateinit var repository: DataRepository

    @Mock
    lateinit var onDataLoadedListener: OnDataLoadedListener<List<Recipe>>

    @InjectMocks
    lateinit var viewModel: FavoritesViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = FavoritesViewModel(context, repository)
        viewModel.setOnDataLoadedListener(onDataLoadedListener)
    }

    @Test
    fun testLoadFavoriteRecipes_emptyList() {
        `when`(repository.getFavoriteRecipes()).thenReturn(Flowable.just(ArrayList()))
        viewModel.start()
        verify(repository, atLeastOnce()).getFavoriteRecipes()
        verify(onDataLoadedListener, atLeastOnce()).onDataLoaded(ArgumentMatchers.anyList())
        Assert.assertEquals(View.VISIBLE, viewModel.emptyViewVisibility.get())
    }

    @Test
    fun testLoadFavoriteRecipes_notEmptyList() {
        val recipes = ArrayList<Recipe>()
        `when`(repository.getFavoriteRecipes()).thenReturn(Flowable.just(recipes))
        recipes.add(Recipe())
        viewModel.start()
        verify(repository, atLeastOnce()).getFavoriteRecipes()
        verify(onDataLoadedListener, atLeastOnce()).onDataLoaded(recipes)
        Assert.assertEquals(View.INVISIBLE, viewModel.emptyViewVisibility.get())
    }
}