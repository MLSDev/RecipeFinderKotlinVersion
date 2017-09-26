package com.mlsdev.recipefinder.kotlinversion.view.analysenutrition.ingredient

import android.content.Context
import android.view.View
import android.view.inputmethod.EditorInfo
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.mlsdev.recipefinder.kotlinversion.BuildConfig
import com.mlsdev.recipefinder.kotlinversion.data.entity.nutrition.TotalNutrients
import com.mlsdev.recipefinder.kotlinversion.data.source.repository.DataRepository
import com.mlsdev.recipefinder.kotlinversion.view.ActionListener
import com.mlsdev.recipefinder.kotlinversion.view.OnKeyboardStateChangedListener
import com.mlsdev.recipefinder.kotlinversion.view.listener.OnIngredientAnalyzedListener
import com.mlsdev.recipefinder.kotlinversion.view.utils.DiagramUtils
import getNutrientAnalysisResult
import io.reactivex.Single
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
class IngredientAnalysisViewModelTest {

    @Mock
    lateinit var context: Context

    @Mock
    lateinit var repository: DataRepository

    @Mock
    lateinit var diagramUtils: DiagramUtils

    @Mock
    lateinit var keyboardListener: OnKeyboardStateChangedListener

    @Mock
    lateinit var actionListener: ActionListener

    @Mock
    lateinit var onIngredientAnalyzedListener: OnIngredientAnalyzedListener

    lateinit var viewModel: IngredientAnalysisViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = spy(IngredientAnalysisViewModel(context, diagramUtils, repository))
        viewModel.setKeyboardListener(keyboardListener)
        viewModel.setOnIngredientAnalyzedListener(onIngredientAnalyzedListener)
        viewModel.actionListener = actionListener
    }

    @Test
    fun testOnAnalyzeButtonClick_emptyIngredientInput() {
        doNothing().`when`(actionListener).showSnackbar(anyInt(), anyInt(), any(View.OnClickListener::class.java))
        viewModel.onAnalyzeButtonClick(null)
        verify(keyboardListener, only()).hideKeyboard()
        verify(actionListener, only()).showSnackbar(anyInt(), anyInt(), any(View.OnClickListener::class.java))
    }

    @Test
    fun testOnAnalyzeButtonClick() {
        val analysisResult = getNutrientAnalysisResult()
        `when`(repository.getIngredientData(anyMap())).thenReturn(Single.just(analysisResult))
        doNothing().`when`(viewModel).prepareDiagramData(any())
        viewModel.ingredientText.set("1 small orange")
        viewModel.onAnalyzeButtonClick(null)

        verify(keyboardListener, only()).hideKeyboard()
        verify(actionListener, atLeastOnce()).showProgressDialog(false, null)
        assertEquals(View.VISIBLE, viewModel.analysisResultsWrapperVisibility.get())
        assertEquals(viewModel.ingredientText.get(), viewModel.nutrientText.get())
        assertEquals(analysisResult.totalNutrients.fat!!.getFormattedFullText(), viewModel.fatText.get())
        assertEquals(analysisResult.totalNutrients.carbs!!.getFormattedFullText(), viewModel.carbsText.get())
        assertEquals(analysisResult.totalNutrients.protein!!.getFormattedFullText(), viewModel.proteinText.get())
        assertEquals(analysisResult.totalNutrients.energy!!.getFormattedFullText(), viewModel.energyText.get())
        assertEquals(analysisResult.totalNutrients, viewModel.totalNutrients)
        assertEquals(View.VISIBLE, viewModel.fatLabelVisibility.get())
        assertEquals(View.VISIBLE, viewModel.carbsLabelVisibility.get())
        assertEquals(View.VISIBLE, viewModel.proteinLabelVisibility.get())
        assertEquals(View.VISIBLE, viewModel.energyLabelVisibility.get())
    }

    @Test
    fun testOnEditorAction() {
        doNothing().`when`(viewModel).onAnalyzeButtonClick(null)
        viewModel.onEditorAction(null, EditorInfo.IME_ACTION_DONE, null)
        verify(keyboardListener, only()).hideKeyboard()
    }

    @Test
    fun testPrepareDiagramData() {
        val totalNutrients = TotalNutrients()
        val pieEntries = ArrayList<PieEntry>(1)
        val pieDataSet = PieDataSet(pieEntries, "")
        val pieData = PieData()
        pieEntries.add(PieEntry(0F))
        doNothing().`when`(onIngredientAnalyzedListener).onIngredientAnalyzed(any())
        `when`(diagramUtils.preparePieEntries(any(TotalNutrients::class.java))).thenReturn(pieEntries)
        `when`(diagramUtils.createPieDataSet(eq(pieEntries), eq("Nutrients"), eq(null))).thenReturn(pieDataSet)
        `when`(diagramUtils.createPieData(pieDataSet)).thenReturn(pieData)

        viewModel.prepareDiagramData(totalNutrients)

        verify(diagramUtils, atLeastOnce()).preparePieEntries(any(TotalNutrients::class.java))
        verify(diagramUtils, atLeastOnce()).createPieDataSet(eq(pieEntries), eq("Nutrients"), eq(null))
        verify(diagramUtils, atLeastOnce()).createPieData(pieDataSet)
        verify(onIngredientAnalyzedListener, atLeastOnce()).onIngredientAnalyzed(any())
    }
}
