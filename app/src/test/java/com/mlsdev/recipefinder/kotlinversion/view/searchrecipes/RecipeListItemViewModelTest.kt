package com.mlsdev.recipefinder.kotlinversion.view.searchrecipes

import com.mlsdev.recipefinder.kotlinversion.BuildConfig
import com.mlsdev.recipefinder.kotlinversion.data.entity.recipe.Recipe
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, manifest = Config.NONE, sdk = intArrayOf(26))
class RecipeListItemViewModelTest {
    lateinit var recipe: Recipe
    lateinit var viewModel: RecipeListItemViewModel

    @Before
    fun setUp() {
        recipe = Recipe()
        viewModel = RecipeListItemViewModel(recipe)
    }

    @Test
    fun testSetRecipe() {
        recipe.image ="https://image-url.com"
        recipe.label = "Recipe label"
        viewModel.setRecipe(recipe)
        assertEquals(recipe.image, viewModel.recipeImageUrl.get())
        assertEquals(recipe.label, viewModel.recipeTitle.get())
    }

    @Test
    fun testGetRecipe() {
        val recipeFromViewModel = viewModel.getRecipe()
        assertEquals(recipe, recipeFromViewModel)
    }
}