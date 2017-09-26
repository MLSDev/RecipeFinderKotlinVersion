package com.mlsdev.recipefinder.kotlinversion.view.analysenutrition.recipe

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.mlsdev.recipefinder.kotlinversion.BuildConfig
import com.mlsdev.recipefinder.kotlinversion.R
import com.mlsdev.recipefinder.kotlinversion.data.entity.nutrition.NutritionAnalysisResult
import com.mlsdev.recipefinder.kotlinversion.data.entity.nutrition.TotalNutrients
import com.mlsdev.recipefinder.kotlinversion.view.listener.OnDataLoadedListener
import com.mlsdev.recipefinder.kotlinversion.view.utils.DiagramUtils
import getRecipeAnalysisResult
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, manifest = Config.NONE, sdk = intArrayOf(26))
class RecipeAnalysisDetailsViewModelTest {

    @Mock
    lateinit var context: Context

    @Mock
    lateinit var resources: Resources

    @Mock
    lateinit var diagramUtils: DiagramUtils

    @Mock
    lateinit var onDataLoadedListener: OnDataLoadedListener<PieData>

    lateinit var viewModel: RecipeAnalysisDetailsViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = spy(RecipeAnalysisDetailsViewModel(context, diagramUtils))
        viewModel.setOnDataLoadedListener(onDataLoadedListener)
        `when`(context.resources).thenReturn(resources)
    }

    @Test
    fun testSetData() {
        doNothing().`when`(viewModel).showResults(any(NutritionAnalysisResult::class.java))
        val data = Bundle()
        data.putParcelable(RecipeAnalysisDetailsActivity.RECIPE_ANALYSING_RESULT_KEY, NutritionAnalysisResult())
        viewModel.setData(data)
        verify(viewModel, atLeastOnce()).showResults(any(NutritionAnalysisResult::class.java))
    }

    @Test
    fun setData_withoutActualData() {
        viewModel.setData(Bundle())
        verify(viewModel, never()).showResults(any(NutritionAnalysisResult::class.java))
    }

    @Test
    fun testShowResults() {
        val analysisResult = getRecipeAnalysisResult()
        val pieEntries = ArrayList<PieEntry>(1)
        val pieDataSet = PieDataSet(pieEntries, "")
        val pieData = PieData()
        pieEntries.add(PieEntry(0F))

        `when`(resources.getString(R.string.calories, analysisResult.calories)).thenReturn("Calories: ${analysisResult.calories}")
        `when`(resources.getString(eq(R.string.yields), eq(analysisResult.yields.toString()))).thenReturn("Yields: ${analysisResult.yields}")
        `when`(diagramUtils.preparePieEntries(any(TotalNutrients::class.java))).thenReturn(pieEntries)
        `when`(diagramUtils.createPieDataSet(eq(pieEntries), eq("Nutrients"), eq(null))).thenReturn(pieDataSet)
        `when`(diagramUtils.createPieData(pieDataSet)).thenReturn(pieData)
        doNothing().`when`(onDataLoadedListener).onDataLoaded(pieData)

        viewModel.showResults(analysisResult)

        assertEquals("Calories: ${analysisResult.calories}", viewModel.calories.get())
        assertEquals("Yields: ${analysisResult.yields}", viewModel.yields.get())
        verify(diagramUtils, atLeastOnce()).preparePieEntries(any(TotalNutrients::class.java))
        verify(diagramUtils, atLeastOnce()).createPieDataSet(eq(pieEntries), eq("Nutrients"), eq(null))
        verify(diagramUtils, atLeastOnce()).createPieData(pieDataSet)
        verify(onDataLoadedListener, atLeastOnce()).onDataLoaded(pieData)
    }
}