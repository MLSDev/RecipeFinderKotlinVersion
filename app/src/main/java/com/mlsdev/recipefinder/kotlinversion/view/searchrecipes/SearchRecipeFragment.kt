package com.mlsdev.recipefinder.kotlinversion.view.searchrecipes

import android.app.Activity
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.view.*
import com.mlsdev.recipefinder.kotlinversion.R
import com.mlsdev.recipefinder.kotlinversion.data.entity.recipe.Recipe
import com.mlsdev.recipefinder.kotlinversion.databinding.FragmentSearchRecipesBinding
import com.mlsdev.recipefinder.kotlinversion.view.ActionListener
import com.mlsdev.recipefinder.kotlinversion.view.fragment.RecipeListFragment
import javax.inject.Inject

class SearchRecipeFragment : RecipeListFragment(), RecipeListAdapter.OnLastItemShownListener,
        SwipeRefreshLayout.OnRefreshListener, ActionListener{

    companion object {
        const val FILTER_REQUEST_CODE = 0
    }

    private lateinit var binding: FragmentSearchRecipesBinding
    private var filterMenuItem: MenuItem? = null
    private var viewModel: SearchViewModel? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_recipes, container, false)
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        if (viewModel == null)
            viewModel = ViewModelProviders.of(this, viewModelFactory).get(SearchViewModel::class.java)

        lifecycle.addObserver(viewModel)
        viewModel!!.actionListener = this
        viewModel!!.setOnDataLoadedListener(this)

        binding.viewModel = viewModel
        swipeRefreshLayout = binding.swipeToRefreshView

        binding.searchView.setOnSearchViewListener(viewModel)

        initRecyclerView(binding.rvRecipeList, true)
        initSwipeRefreshLayout(binding.swipeToRefreshView, this)

        return binding.root
    }

    override fun scrollToTop() {
        val params: CoordinatorLayout.LayoutParams = binding.appbar.layoutParams as CoordinatorLayout.LayoutParams
        val behavior: AppBarLayout.Behavior? = params.behavior as AppBarLayout.Behavior?
        if (behavior != null)
            behavior.onNestedFling(binding.clSearchRecipes, binding.appbar, binding.root, 0F, (-1000).toFloat(), true)

        binding.rvRecipeList.smoothScrollToPosition(0)
    }

    override fun onDestroy() {
        if (viewModel != null)
            viewModel!!.onDestroy()
        super.onDestroy()
    }

    override fun onLastItemShown() {
        if (viewModel != null)
            viewModel!!.loadMoreRecipes()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == FILTER_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null && viewModel != null)
            viewModel!!.onApplyFilterOptions(data.extras)
    }

    override fun onRefresh() {
        if (viewModel != null)
            viewModel!!.refresh()
    }

    override fun onStartFilter() {
        val filterDialogFragment = FilterDialogFragment()
        filterDialogFragment.setTargetFragment(this, FILTER_REQUEST_CODE)
        filterDialogFragment.show(fragmentManager, "Filter")
    }

    override fun onDataLoaded(recipes: List<Recipe>) {
        super.onDataLoaded(recipes)
        if (filterMenuItem != null)
            filterMenuItem!!.isVisible = !recipes.isEmpty()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu, menu)
        filterMenuItem = menu.findItem(R.id.action_filter)
        if (filterMenuItem != null)
            filterMenuItem!!.isVisible = recipeListAdapter!!.itemCount > 0
        val item = menu.findItem(R.id.action_search)
        binding.searchView.setMenuItem(item)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_filter -> {
                viewModel!!.onFilterClick(null)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }
}
