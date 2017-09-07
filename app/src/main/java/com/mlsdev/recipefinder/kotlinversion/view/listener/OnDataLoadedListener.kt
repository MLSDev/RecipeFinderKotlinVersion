package com.mlsdev.recipefinder.kotlinversion.view.listener

interface OnDataLoadedListener<T> {
    fun onDataLoaded(recipes: T)

    fun onMoreDataLoaded(moreRecipes: T)
}
