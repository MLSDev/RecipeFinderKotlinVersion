package com.mlsdev.recipefinder.kotlinversion.view.analysenutrition.recipe

import android.app.Activity
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mlsdev.recipefinder.kotlinversion.R
import com.mlsdev.recipefinder.kotlinversion.databinding.FragmentRecipeAnalysisBinding
import com.mlsdev.recipefinder.kotlinversion.di.Injectable
import com.mlsdev.recipefinder.kotlinversion.view.fragment.BaseFragment
import com.mlsdev.recipefinder.kotlinversion.view.listener.OnDataLoadedListener
import javax.inject.Inject

class RecipeAnalysisFragment : BaseFragment(), OnAddIngredientClickListener,
        OnDataLoadedListener<List<String>>, Injectable {
    private lateinit var binding: FragmentRecipeAnalysisBinding
    private lateinit var adapter: IngredientsAdapter
    private var viewModel: RecipeAnalysisViewModel? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_analysis, container, false)

        if (viewModel == null)
            viewModel = ViewModelProviders.of(this, viewModelFactory).get(RecipeAnalysisViewModel::class.java)

        viewModel!!.setAddIngredientListener(this)
        viewModel!!.setDataLoadedListener(this)
        viewModel!!.actionListener = this

        lifecycle.addObserver(viewModel)

        binding.viewModel = viewModel
        initRecyclerView()
        return binding.root
    }

    private fun initRecyclerView() {
        adapter = IngredientsAdapter(this)
        binding.rvIngredients.setHasFixedSize(true)
        binding.rvIngredients.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.rvIngredients.adapter = adapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == RecipeAnalysisViewModel.ADD_INGREDIENT_REQUEST_CODE
                && resultCode == Activity.RESULT_OK && data != null) {
            adapter.addItem(data)
            binding.rvIngredients.smoothScrollToPosition(adapter.itemCount)
            if (viewModel != null)
                viewModel!!.setIngredients(adapter.getIngredientList())
        }

    }

    override fun onAddIngredientButtonClick() {
        val dialogFragment = AddIngredientDialogFragment()
        dialogFragment.setTargetFragment(this, RecipeAnalysisViewModel.ADD_INGREDIENT_REQUEST_CODE)
        dialogFragment.show(fragmentManager, "add_ingredient")
    }

    override fun onDataLoaded(ingredients: List<String>) {
        adapter.setData(ingredients)
    }

    override fun onMoreDataLoaded(moreData: List<String>) {
    }
}
