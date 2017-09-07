package com.mlsdev.recipefinder.kotlinversion.view.fragment

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.transition.Fade
import android.transition.TransitionInflater
import android.widget.ImageView
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable
import com.mlsdev.recipefinder.kotlinversion.R
import com.mlsdev.recipefinder.kotlinversion.data.entity.recipe.Recipe
import com.mlsdev.recipefinder.kotlinversion.view.Extras
import com.mlsdev.recipefinder.kotlinversion.view.listener.OnDataLoadedListener
import com.mlsdev.recipefinder.kotlinversion.view.recipedetails.RecipeDetailsFragment
import com.mlsdev.recipefinder.kotlinversion.view.searchrecipes.RecipeListAdapter
import com.mlsdev.recipefinder.kotlinversion.view.utils.UtilsUI
import javax.inject.Inject

open class RecipeListFragment : TabFragment(), RecipeListAdapter.OnItemClickListener,
        RecipeListAdapter.OnLastItemShownListener, OnDataLoadedListener<List<Recipe>> {
    protected var recipeListAdapter: RecipeListAdapter? = null
    protected var swipeRefreshLayout: SwipeRefreshLayout? = null
    protected lateinit var recipeRecyclerView: RecyclerView

    @Inject
    lateinit var utilsUI: UtilsUI

    override fun onItemClicked(recipe: Recipe, imageView: ImageView) {
        val recipeData = Bundle()
        val fragment = RecipeDetailsFragment()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sharedElementReturnTransition = TransitionInflater.from(activity)
                    .inflateTransition(R.transition.change_image_transform)
            exitTransition = Fade()

            fragment.sharedElementEnterTransition = TransitionInflater.from(activity)
                    .inflateTransition(R.transition.change_image_transform)
            fragment.enterTransition = Fade()
        }

        recipeData.putParcelable(Extras.DATA, recipe)

        if (imageView.drawable != null) {
            val bitmapImage: Bitmap = if (imageView.drawable is GlideBitmapDrawable)
                (imageView.drawable as GlideBitmapDrawable).bitmap
            else
                (imageView.drawable as BitmapDrawable).bitmap
            recipeData.putParcelable(Extras.IMAGE_DATA, bitmapImage)
        }

        recipeData.putString(Extras.IMAGE_TRANSITION_NAME, ViewCompat.getTransitionName(imageView))

        fragment.arguments = recipeData

        fragmentManager
                .beginTransaction()
                .addToBackStack("RecipeDetails")
                .replace(R.id.fl_content, fragment)
                .addSharedElement(imageView, ViewCompat.getTransitionName(imageView))
                .commit()
    }

    override fun onLastItemShown() {
    }

    protected fun initRecyclerView(recyclerView: RecyclerView) {
        val columns = activity.resources.configuration.orientation

        if (recipeListAdapter == null)
            recipeListAdapter = RecipeListAdapter(this, this)

        recipeRecyclerView = recyclerView
        recipeRecyclerView.layoutManager = GridLayoutManager(activity, columns, GridLayoutManager.VERTICAL, false)
        recipeRecyclerView.setHasFixedSize(true)
        recipeRecyclerView.adapter = recipeListAdapter
    }

    protected fun initSwipeRefreshLayout(refreshLayout: SwipeRefreshLayout, listener: SwipeRefreshLayout.OnRefreshListener) {
        swipeRefreshLayout = refreshLayout
        swipeRefreshLayout!!.setOnRefreshListener(listener)
        swipeRefreshLayout!!.setColorSchemeColors(
                utilsUI.getColor(R.color.colorPrimaryDark),
                utilsUI.getColor(R.color.colorPrimary),
                utilsUI.getColor(R.color.colorAccent)
        )
    }

    override fun onDataLoaded(recipes: List<Recipe>) {
        val animate = recipeListAdapter!!.isEmpty()
        recipeListAdapter!!.setData(recipes)
        if (animate) recipeRecyclerView.scheduleLayoutAnimation()
        stopRefreshing()
    }

    override fun onMoreDataLoaded(moreRecipes: List<Recipe>) {
        recipeListAdapter!!.setMoreData(moreRecipes)
        stopRefreshing()
    }

    protected fun stopRefreshing() {
        if (swipeRefreshLayout != null)
            swipeRefreshLayout!!.isRefreshing = false
    }
}
