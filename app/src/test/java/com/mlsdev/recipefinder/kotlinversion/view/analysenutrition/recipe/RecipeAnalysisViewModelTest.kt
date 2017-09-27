package com.mlsdev.recipefinder.kotlinversion.view.analysenutrition.recipe

import android.content.Context
import android.content.res.Resources
import com.mlsdev.recipefinder.kotlinversion.BuildConfig
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, manifest = Config.NONE, sdk = intArrayOf(26))
class RecipeAnalysisViewModelTest {

    @Mock
    lateinit var context: Context

    @Mock
    lateinit var resources: Resources

    lateinit var viewModel: RecipeAnalysisViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

}