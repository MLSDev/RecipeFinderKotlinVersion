package com.mlsdev.recipefinder.kotlinversion.view.searchrecipes

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.os.Bundle
import android.support.v4.util.ArrayMap
import android.util.Log
import android.view.View
import com.claudiodegio.msv.OnSearchViewListener
import com.mlsdev.recipefinder.kotlinversion.R
import com.mlsdev.recipefinder.kotlinversion.data.entity.recipe.Recipe
import com.mlsdev.recipefinder.kotlinversion.data.source.remote.ParameterKeys
import com.mlsdev.recipefinder.kotlinversion.data.source.repository.DataRepository
import com.mlsdev.recipefinder.kotlinversion.view.MainActivity
import com.mlsdev.recipefinder.kotlinversion.view.listener.OnDataLoadedListener
import com.mlsdev.recipefinder.kotlinversion.view.utils.ParamsHelper
import com.mlsdev.recipefinder.kotlinversion.view.viewmodel.BaseViewModel
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SearchViewModel @Inject
constructor(context: Context, override var repository: DataRepository) : BaseViewModel(context), OnSearchViewListener, LifecycleObserver {
    val loadMoreProgressBarVisibility = ObservableInt(View.INVISIBLE)
    val searchLabelVisibility = ObservableInt(View.VISIBLE)
    val searchText = ObservableField<String>()
    val searchLabelText = ObservableField<String>(context.getString(R.string.label_search))
    private lateinit var onDataLoadedListener: OnDataLoadedListener<List<Recipe>>
    private val searchParams = ArrayMap<String, String>()
    val isSearchOpened = ObservableBoolean(false)
    private var query = ""
    private val recipes = ArrayList<Recipe>()

    fun setOnDataLoadedListener(onDataLoadedListener: OnDataLoadedListener<List<Recipe>>) {
        this.onDataLoadedListener = onDataLoadedListener
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun start() {
        populateRecipeList()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        Log.d("RF", "lifecycle stop")
    }

    private fun populateRecipeList() {
        if (!recipes.isEmpty()) {
            onDataLoadedListener.onDataLoaded(recipes)
            searchLabelVisibility.set(View.INVISIBLE)
        }
    }

    /**
     * @param searchText  The search phrase inputted by a user
     * @param forceUpdate A force update flag. If it is "true" cache will be cleaned, else a user will
     *                    see cached data.
     */
    fun searchRecipes(searchText: String?, forceUpdate: Boolean) {
        if (searchText == null || searchText.isEmpty()) {
            onDataLoadedListener.onDataLoaded(ArrayList())
            return
        }

        query = searchText

        val prevSearchText = if (searchParams.containsKey(ParameterKeys.QUERY)) searchParams.get(ParameterKeys.QUERY) else ""

        if (forceUpdate || !(prevSearchText.equals(searchText.toLowerCase())))
            repository.cacheIsDirty = true

        searchParams.put(ParameterKeys.QUERY, query.toLowerCase())
        subscriptions.clear()
        searchLabelVisibility.set(View.INVISIBLE)
        repository.searchRecipes(searchParams)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : SearchRecipesObserver<List<Recipe>>() {
                    override fun onSuccess(recipeList: List<Recipe>) {
                        actionListener!!.showProgressDialog(false, null)
                        recipes.clear()
                        recipes.addAll(recipeList)
                        val commonSearchLabelText = context.getString(R.string.label_search)
                        val nothingFoundText = context.getString(R.string.label_search_nothing_found)
                        searchLabelText.set(if (recipes.isEmpty()) nothingFoundText else commonSearchLabelText)
                        searchLabelVisibility.set(if (recipes.isEmpty()) View.VISIBLE else View.INVISIBLE)
                        populateRecipeList()
                    }
                })
    }

    fun loadMoreRecipes() {
        val params = ArrayMap<String, String>()
        params.put(ParameterKeys.QUERY, query.toLowerCase())
        subscriptions.clear()
        loadMoreProgressBarVisibility.set(View.VISIBLE)

        repository.loadMore(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : SearchRecipesObserver<List<Recipe>>() {
                    override fun onSuccess(recipeList: List<Recipe>) {
                        onDataLoadedListener.onMoreDataLoaded(recipes)
                        recipes.clear()
                        recipes.addAll(recipeList)
                    }
                })

    }

    fun refresh() {
        searchRecipes(query, true)
    }

    /**
     * Makes a search with applied searching parameters
     *
     * @param filterData The {@link Bundle} object with applied parameters for searching
     */
    fun onApplyFilterOptions(filterData: Bundle) {
        val healthLabel = ParamsHelper.Companion.formatLabel(filterData.getString(FilterDialogFragment.HEALTH_LABEL_KEY))
        val dietLabel = ParamsHelper.Companion.formatLabel(filterData.getString(FilterDialogFragment.DIET_LABEL_KEY))

        searchParams.put(ParameterKeys.HEALTH, healthLabel)
        searchParams.put(ParameterKeys.DIET, dietLabel)

        searchRecipes(query, true)
    }

    fun onFilterClick(view: View?) {
        actionListener!!.onStartFilter()
    }

    override fun onSearchViewShown() {
        isSearchOpened.set(true)
    }

    override fun onSearchViewClosed() {
        isSearchOpened.set(false)
    }

    override fun onQueryTextSubmit(s: String): Boolean {
        actionListener!!.showProgressDialog(true, "Searching recipes for " + s)
        searchRecipes(s, true)
        return false
    }

    override fun onQueryTextChange(s: String) {
        searchText.set(s)
    }

    inner abstract class SearchRecipesObserver<T> : SingleObserver<T> {

        override fun onSubscribe(d: Disposable) {
            loadMoreProgressBarVisibility.set(View.INVISIBLE)
            subscriptions.add(d)
        }

        override fun onError(e: Throwable) {
            loadMoreProgressBarVisibility.set(View.INVISIBLE)
            actionListener!!.showProgressDialog(false, null)
            Log.d(MainActivity.LOG_TAG, e.message)
            showError(e)
        }
    }
}
