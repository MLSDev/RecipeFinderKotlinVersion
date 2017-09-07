package com.mlsdev.recipefinder.kotlinversion.view.analysenutrition.ingredient

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.data.PieData
import com.mlsdev.recipefinder.kotlinversion.R
import com.mlsdev.recipefinder.kotlinversion.databinding.FragmentIngredientAnalysisBinding
import com.mlsdev.recipefinder.kotlinversion.di.Injectable
import com.mlsdev.recipefinder.kotlinversion.view.MainActivity
import com.mlsdev.recipefinder.kotlinversion.view.OnKeyboardStateChangedListener
import com.mlsdev.recipefinder.kotlinversion.view.fragment.BaseFragment
import com.mlsdev.recipefinder.kotlinversion.view.listener.OnIngredientAnalyzedListener
import com.mlsdev.recipefinder.kotlinversion.view.utils.DiagramUtils
import javax.inject.Inject

class IngredientAnalysisFragment : BaseFragment(), OnIngredientAnalyzedListener,
        OnKeyboardStateChangedListener, Injectable {
    private lateinit var binding: FragmentIngredientAnalysisBinding
    private var viewModel: IngredientAnalysisViewModel? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var diagramUtils: DiagramUtils


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ingredient_analysis, container, false)

        if (viewModel == null)
            viewModel = ViewModelProviders.of(this, viewModelFactory).get(IngredientAnalysisViewModel::class.java)

        lifecycle.addObserver(viewModel)
        viewModel!!.setKeyboardListener(this)
        viewModel!!.setOnIngredientAnalyzedListener(this)
        viewModel!!.actionListener = this

        binding.viewModel = viewModel
        diagramUtils.preparePieChart(binding.pieChart)
        return binding.root
    }

    override fun onStop() {
        super.onStop()
        binding.etIngredientInput.clearFocus()
    }

    override fun onIngredientAnalyzed(diagramData: PieData) {
        diagramUtils.setData(binding.pieChart, diagramData)
    }

    override fun showKeyboard() {
        (activity as MainActivity).showSoftKeyboard()
    }

    override fun hideKeyboard() {
        (activity as MainActivity).hideSoftKeyboard()
    }
}
