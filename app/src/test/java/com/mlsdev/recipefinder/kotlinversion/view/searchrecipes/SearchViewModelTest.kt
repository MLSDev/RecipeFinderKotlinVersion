package com.mlsdev.recipefinder.kotlinversion.view.searchrecipes

import android.content.Context
import android.os.Bundle
import android.view.View
import com.mlsdev.recipefinder.kotlinversion.BuildConfig
import com.mlsdev.recipefinder.kotlinversion.data.entity.recipe.Recipe
import com.mlsdev.recipefinder.kotlinversion.data.source.repository.DataRepository
import com.mlsdev.recipefinder.kotlinversion.view.ActionListener
import com.mlsdev.recipefinder.kotlinversion.view.listener.OnDataLoadedListener
import getSearchResult
import io.reactivex.Single
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, manifest = Config.NONE, sdk = intArrayOf(26))
class SearchViewModelTest {

    @Mock
    lateinit var context: Context

    @Mock
    lateinit var repository: DataRepository

    @Mock
    lateinit var onDataLoadedListener: OnDataLoadedListener<List<Recipe>>

    @Mock
    lateinit var actionListener: ActionListener

    lateinit var viewModel: SearchViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        viewModel = SearchViewModel(context, repository)
        viewModel.setOnDataLoadedListener(onDataLoadedListener)
        viewModel.actionListener = actionListener
    }

    @Test
    fun testSearchRecipes() {
        val recipes = getSearchResult()
        `when`(repository.searchRecipes(ArgumentMatchers.anyMap())).thenReturn(Single.just(recipes))

        viewModel.searchRecipes("query", true)
        verify(repository, atLeastOnce()).searchRecipes(ArgumentMatchers.anyMap())
        verify(actionListener, atLeastOnce()).showProgressDialog(false, null)
        verify(onDataLoadedListener, atLeastOnce()).onDataLoaded(recipes)
        Assert.assertEquals(View.INVISIBLE, viewModel.searchLabelVisibility.get())
    }

    @Test
    fun testSearchRecipes_emptyQuery() {
        viewModel.searchRecipes("", true)
        verify(onDataLoadedListener, only()).onDataLoaded(emptyList())
        verify(repository, never()).searchRecipes(ArgumentMatchers.anyMap())
    }

    @Test
    fun testLoadMoreRecipes_noDataMore() {
        `when`(repository.loadMore(ArgumentMatchers.anyMap())).thenReturn(Single.just(emptyList()))
        viewModel.loadMoreRecipes()
        verify(repository, only()).loadMore(ArgumentMatchers.anyMap())
        verify(onDataLoadedListener, only()).onMoreDataLoaded(emptyList())
    }

    @Test
    fun testLoadMoreRecipes() {
        val recipes = getSearchResult()
        `when`(repository.loadMore(ArgumentMatchers.anyMap())).thenReturn(Single.just(recipes))
        viewModel.loadMoreRecipes()
        verify(repository, only()).loadMore(ArgumentMatchers.anyMap())
        verify(onDataLoadedListener, only()).onMoreDataLoaded(recipes)
    }

    @Test
    fun testOnApplyFilterOptions() {
        val data = Bundle()
        data.putString(FilterDialogFragment.HEALTH_LABEL_KEY, "label")
        data.putString(FilterDialogFragment.DIET_LABEL_KEY, "label")
        `when`(repository.searchRecipes(ArgumentMatchers.anyMap())).thenReturn(Single.just(emptyList()))
        viewModel.query = "query"
        viewModel.onApplyFilterOptions(data)
        verify(repository, atLeastOnce()).searchRecipes(ArgumentMatchers.anyMap())
    }

    @Test
    fun testOnQueryTextSubmit_emptyString() {
        viewModel.onQueryTextSubmit("")
        verify(actionListener, never()).showProgressDialog(true, "Searching recipes for ")
    }

    @Test
    fun testOnQueryTextChange() {
        val text = "test"
        viewModel.onQueryTextChange(text)
        assertEquals(text, viewModel.searchText.get())
    }

    @Test
    fun testOnFilterClick() {
        viewModel.onFilterClick(null)
        verify(actionListener, only()).onStartFilter()
    }

    @Test
    fun testOnSearchViewShown() {
        viewModel.onSearchViewShown()
        assertTrue(viewModel.isSearchOpened.get())
    }

    @Test
    fun testOnSearchViewClosed() {
        viewModel.onSearchViewClosed()
        assertFalse(viewModel.isSearchOpened.get())
    }

    @Test
    fun testRefresh() {
        `when`(repository.searchRecipes(ArgumentMatchers.anyMap())).thenReturn(Single.just(emptyList()))
        viewModel.query = "query"
        viewModel.refresh()
        verify(repository, only()).searchRecipes(ArgumentMatchers.anyMap())
    }
}