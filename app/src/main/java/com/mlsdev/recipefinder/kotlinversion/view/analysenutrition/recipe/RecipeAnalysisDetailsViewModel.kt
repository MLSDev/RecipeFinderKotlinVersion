package com.mlsdev.recipefinder.kotlinversion.view.analysenutrition.recipe

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.os.Bundle
import android.util.Log
import android.view.View
import com.github.mikephil.charting.data.PieData
import com.mlsdev.recipefinder.kotlinversion.R
import com.mlsdev.recipefinder.kotlinversion.data.entity.nutrition.NutritionAnalysisResult
import com.mlsdev.recipefinder.kotlinversion.view.listener.OnDataLoadedListener
import com.mlsdev.recipefinder.kotlinversion.view.utils.DiagramUtils
import com.mlsdev.recipefinder.kotlinversion.view.viewmodel.BaseViewModel
import javax.inject.Inject

class RecipeAnalysisDetailsViewModel @Inject
constructor(context: Context, private val diagramUtils: DiagramUtils) : BaseViewModel(context), LifecycleObserver {

    val calories = ObservableField<String>()
    val yields = ObservableField<String>()
    val chartVisibility = ObservableInt(View.GONE)
    private lateinit var pieData: PieData
    private var onDataLoadedListener: OnDataLoadedListener<PieData>? = null

    fun setOnDataLoadedListener(onDataLoadedListener: OnDataLoadedListener<PieData>) {
        this.onDataLoadedListener = onDataLoadedListener
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun start() {
        if (onDataLoadedListener != null)
            onDataLoadedListener!!.onDataLoaded(pieData)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        Log.d("RF", "lifecycle stop")
    }

    fun setData(recipeAnalysingData: Bundle) {
        val nutritionAnalysisResult: NutritionAnalysisResult = recipeAnalysingData
                .getParcelable(RecipeAnalysisDetailsActivity.RECIPE_ANALYSING_RESULT_KEY)

        showResults(nutritionAnalysisResult)
    }

    private fun showResults(nutritionAnalysisResult: NutritionAnalysisResult?) {
        if (nutritionAnalysisResult == null)
            return

        calories.set(context.getString(R.string.calories, nutritionAnalysisResult.calories))
        yields.set(context.getString(R.string.yields, nutritionAnalysisResult.yields.toString()))

        val pieEntries = diagramUtils.preparePieEntries(nutritionAnalysisResult.totalNutrients)
        chartVisibility.set(if (pieEntries.isEmpty()) View.GONE else View.VISIBLE)

        if (pieEntries.isEmpty())
            return

        val pieDataSet = diagramUtils.createPieDataSet(pieEntries, "Nutrients", null)
        pieData = diagramUtils.createPieData(pieDataSet)

        if (onDataLoadedListener != null)
            onDataLoadedListener!!.onDataLoaded(pieData)
    }

}
