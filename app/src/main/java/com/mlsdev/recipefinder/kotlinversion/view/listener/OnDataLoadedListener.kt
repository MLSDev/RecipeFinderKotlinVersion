package com.mlsdev.recipefinder.kotlinversion.view.listener

open interface OnDataLoadedListener<T> {
    fun onDataLoaded(recipes: T)

    fun onMoreDataLoaded(moreRecipes: T)
}
