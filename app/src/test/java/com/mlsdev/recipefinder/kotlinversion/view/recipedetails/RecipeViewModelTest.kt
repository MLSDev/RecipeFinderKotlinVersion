package com.mlsdev.recipefinder.kotlinversion.view.recipedetails

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import com.mlsdev.recipefinder.kotlinversion.BuildConfig
import com.mlsdev.recipefinder.kotlinversion.R
import com.mlsdev.recipefinder.kotlinversion.data.entity.recipe.Ingredient
import com.mlsdev.recipefinder.kotlinversion.data.entity.recipe.Recipe
import com.mlsdev.recipefinder.kotlinversion.data.source.repository.DataRepository
import com.mlsdev.recipefinder.kotlinversion.getRecipe
import com.mlsdev.recipefinder.kotlinversion.view.Extras
import com.mlsdev.recipefinder.kotlinversion.view.utils.UtilsUI
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, manifest = Config.NONE, sdk = intArrayOf(26))
class RecipeViewModelTest {

    @Mock
    lateinit var context: Context

    @Mock
    lateinit var resources: Resources

    @Mock
    lateinit var repository: DataRepository

    lateinit var viewModel: RecipeViewModel

    lateinit var recipe: Recipe

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        `when`(context.resources).thenReturn(resources)
        recipe = getRecipe()
        viewModel = RecipeViewModel(context, repository)
        val bundle = Bundle()
        bundle.putParcelable(Extras.DATA, recipe)
        viewModel.setRecipeData(bundle)
    }

    @Test
    fun testSetData() {
        assertEquals(recipe.label, viewModel.recipeTitle.get())
        assertEquals(recipe.image, viewModel.recipeImageUrl.get())
        assertEquals(viewModel.getLabelsAsString(recipe.healthLabels), viewModel.recipeHealthLabels.get())
        assertEquals(viewModel.getLabelsAsString(recipe.dietLabels), viewModel.recipeDietLabels.get())
        assertEquals(viewModel.getIngredientsAsString(recipe.ingredients), viewModel.recipeIngredients.get())
        assertEquals(
                UtilsUI.Companion.getPercents(recipe.totalWeight, recipe.totalNutrients.protein!!.quantity),
                viewModel.proteinProgressValue.get()
        )
        assertEquals(
                UtilsUI.Companion.getPercents(recipe.totalWeight, recipe.totalNutrients.carbs!!.quantity),
                viewModel.carbsProgressValue.get()
        )
        assertEquals(
                UtilsUI.Companion.getPercents(recipe.totalWeight, recipe.totalNutrients.fat!!.quantity),
                viewModel.fatProgressValue.get()
        )
    }

    @Test
    fun testCheckIsTheRecipeInFavorites_true() {
        `when`(repository.isInFavorites(recipe)).thenReturn(Single.just(true))
        viewModel.onStart()
        verify(repository, atLeastOnce()).isInFavorites(recipe)
        assertTrue(viewModel.favoriteImageStateChecked.get())
    }

    @Test
    fun testCheckIsTheRecipeInFavorites_false() {
        `when`(repository.isInFavorites(recipe)).thenReturn(Single.just(false))
        viewModel.onStart()
        verify(repository, atLeastOnce()).isInFavorites(recipe)
        assertFalse(viewModel.favoriteImageStateChecked.get())
    }

    @Test
    fun testAddToFavorites() {
        `when`(repository.addToFavorites(recipe)).thenReturn(Completable.complete())
        viewModel.favoriteImageStateChecked.set(false)
        viewModel.onFavoriteButtonClick(null)
        verify(repository, atLeastOnce()).addToFavorites(recipe)
        assertTrue(viewModel.favoriteImageStateChecked.get())
    }

    @Test
    fun testRemoveFromFavorites() {
        `when`(repository.removeFromFavorites(recipe)).thenReturn(Completable.complete())
        viewModel.favoriteImageStateChecked.set(true)
        viewModel.onFavoriteButtonClick(null)
        verify(repository, atLeastOnce()).removeFromFavorites(recipe)
        assertFalse(viewModel.favoriteImageStateChecked.get())
    }

    @Test
    fun testGetLabelsAsString() {
        val labels = ArrayList<String>(2)
        labels.add("one")
        labels.add("two")
        var expectedResult = "${labels[0]}, ${labels[1]}"
        var actualResult = viewModel.getLabelsAsString(labels)
        assertEquals(expectedResult, actualResult)

        labels.removeAt(1)
        expectedResult = labels[0]
        actualResult = viewModel.getLabelsAsString(labels)
        assertEquals(expectedResult, actualResult)

        labels.removeAt(0)
        expectedResult = ""
        actualResult = viewModel.getLabelsAsString(labels)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun testGetIngredientsAsString() {
        val ingredients = ArrayList<Ingredient>(2)
        val ingredient = Ingredient()
        ingredient.weight = 10.0
        ingredient.text = "One"
        ingredients.add(ingredient)
        val ingredient1 = Ingredient()
        ingredient1.weight = 15.0
        ingredient1.text = "Two"
        ingredients.add(ingredient1)

        `when`(context.getString(R.string.ingredient_item, ingredient.text, UtilsUI.formatDecimalToString(ingredient.weight)))
                .thenReturn("${ingredient.text}; Weight: ${UtilsUI.formatDecimalToString(ingredient.weight)}")

        `when`(context.getString(R.string.ingredient_item, ingredient1.text, UtilsUI.formatDecimalToString(ingredient1.weight)))
                .thenReturn("${ingredient1.text}; Weight: ${UtilsUI.formatDecimalToString(ingredient1.weight)}")

        var expectedResult = "${ingredients[0].text}; Weight: ${UtilsUI.formatDecimalToString(ingredients[0].weight)},\n" +
                "${ingredients[1].text}; Weight: ${UtilsUI.formatDecimalToString(ingredients[1].weight)}"
        var actualResult = viewModel.getIngredientsAsString(ingredients)
        assertEquals(expectedResult, actualResult)
    }

}