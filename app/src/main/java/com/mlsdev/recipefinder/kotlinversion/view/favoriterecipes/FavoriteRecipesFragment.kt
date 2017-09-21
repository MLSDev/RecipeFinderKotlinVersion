package com.mlsdev.recipefinder.kotlinversion.view.favoriterecipes

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mlsdev.recipefinder.kotlinversion.R
import com.mlsdev.recipefinder.kotlinversion.databinding.FragmentFavoriteRecipesBinding
import com.mlsdev.recipefinder.kotlinversion.di.Injectable
import com.mlsdev.recipefinder.kotlinversion.view.fragment.RecipeListFragment
import com.mlsdev.recipefinder.kotlinversion.view.searchrecipes.RecipeListAdapter
import javax.inject.Inject

class FavoriteRecipesFragment : RecipeListFragment(), RecipeListAdapter.OnLastItemShownListener,
        Injectable {
    private lateinit var binding: FragmentFavoriteRecipesBinding
    private var viewModel: FavoritesViewModel? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite_recipes, container, false)
        initRecyclerView(binding.rvRecipeList, false)

        if (viewModel == null)
            viewModel = ViewModelProviders.of(this, viewModelFactory).get(FavoritesViewModel::class.java)

        lifecycle.addObserver(viewModel)
        viewModel!!.setOnDataLoadedListener(this)
        binding.viewModel = viewModel

        return binding.root
    }

    override fun scrollToTop() {
        binding.rvRecipeList.smoothScrollToPosition(0)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (viewModel != null)
            viewModel!!.onDestroy()
    }
}
