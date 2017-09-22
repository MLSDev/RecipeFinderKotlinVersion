package com.mlsdev.recipefinder.kotlinversion.view.favoriterecipes

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.databinding.ObservableInt
import android.view.View
import com.mlsdev.recipefinder.kotlinversion.data.entity.recipe.Recipe
import com.mlsdev.recipefinder.kotlinversion.data.source.repository.DataRepository
import com.mlsdev.recipefinder.kotlinversion.view.listener.OnDataLoadedListener
import com.mlsdev.recipefinder.kotlinversion.view.viewmodel.BaseViewModel
import javax.inject.Inject

open class FavoritesViewModel @Inject
constructor(context: Context, override var repository: DataRepository) : BaseViewModel(context), LifecycleObserver {
    private lateinit var onDataLoadedListener: OnDataLoadedListener<List<Recipe>>
    val emptyViewVisibility = ObservableInt(View.VISIBLE)

    fun setOnDataLoadedListener(onDataLoadedListener: OnDataLoadedListener<List<Recipe>>) {
        this.onDataLoadedListener = onDataLoadedListener
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        getFavoriteRecipes()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        subscriptions.clear()
    }

    private fun getFavoriteRecipes() {
        val disposable = repository.getFavoriteRecipes()
                .subscribe({
                    emptyViewVisibility.set(if (it.isEmpty()) View.VISIBLE else View.INVISIBLE)
                    onDataLoadedListener.onDataLoaded(it)
                })

        subscriptions.add(disposable)
    }
}
