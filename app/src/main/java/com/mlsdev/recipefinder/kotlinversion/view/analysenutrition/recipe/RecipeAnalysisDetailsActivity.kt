package com.mlsdev.recipefinder.kotlinversion.view.analysenutrition.recipe

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.MenuItem
import com.github.mikephil.charting.data.PieData
import com.mlsdev.recipefinder.kotlinversion.R
import com.mlsdev.recipefinder.kotlinversion.databinding.ActivityRecipeAnalysisDetailsBinding
import com.mlsdev.recipefinder.kotlinversion.view.BaseActivity
import com.mlsdev.recipefinder.kotlinversion.view.listener.OnDataLoadedListener
import com.mlsdev.recipefinder.kotlinversion.view.utils.DiagramUtils
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class RecipeAnalysisDetailsActivity : BaseActivity(), HasSupportFragmentInjector, LifecycleRegistryOwner, OnDataLoadedListener<PieData> {

    companion object {
        const val RECIPE_ANALYSING_RESULT_KEY = "recipe_analysing_result"
    }

    private lateinit var binding: ActivityRecipeAnalysisDetailsBinding
    private val lifecycleRegistry = LifecycleRegistry(this)
    private var viewModel: RecipeAnalysisDetailsViewModel? = null
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var diagramUtils: DiagramUtils
    @Inject lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_analysis_details)

        if (viewModel == null) {
            viewModel = ViewModelProviders.of(this, viewModelFactory).get(RecipeAnalysisDetailsViewModel::class.java)
            viewModel!!.setData(intent.extras)
        }

        lifecycle.addObserver(viewModel)
        viewModel!!.setOnDataLoadedListener(this)
        binding.viewModel = viewModel
        diagramUtils.preparePieChart(binding.pieChart)

        setSupportActionBar(binding.toolbar)

        if (supportActionBar != null)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun supportFragmentInjector(): DispatchingAndroidInjector<Fragment> = supportFragmentInjector

    override fun getLifecycle(): LifecycleRegistry = lifecycleRegistry

    override fun onDataLoaded(pieData: PieData) {
        diagramUtils.setData(binding.pieChart, pieData)
    }

    override fun onMoreDataLoaded(moreRecipes: PieData) {
    }

}
