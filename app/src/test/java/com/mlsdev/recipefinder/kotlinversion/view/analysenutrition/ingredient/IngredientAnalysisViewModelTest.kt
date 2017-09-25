package com.mlsdev.recipefinder.kotlinversion.view.analysenutrition.ingredient

import android.content.Context
import com.mlsdev.recipefinder.kotlinversion.BuildConfig
import com.mlsdev.recipefinder.kotlinversion.data.source.repository.DataRepository
import com.mlsdev.recipefinder.kotlinversion.view.ActionListener
import com.mlsdev.recipefinder.kotlinversion.view.OnKeyboardStateChangedListener
import com.mlsdev.recipefinder.kotlinversion.view.utils.DiagramUtils
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.only
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

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

    lateinit var viewModel: IngredientAnalysisViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = IngredientAnalysisViewModel(context, diagramUtils, repository)
        viewModel.setKeyboardListener(keyboardListener)
        viewModel.actionListener = actionListener
    }

    @Test
    fun testOnAnalyzeButtonClick_emptyIngredientInput() {
        viewModel.onAnalyzeButtonClick(null)
        verify(keyboardListener, only()).hideKeyboard()
    }
}
