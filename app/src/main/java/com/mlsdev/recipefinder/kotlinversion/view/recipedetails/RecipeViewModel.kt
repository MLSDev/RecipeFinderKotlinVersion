package com.mlsdev.recipefinder.kotlinversion.view.recipedetails

import android.content.Context
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import android.os.Bundle
import android.view.View
import com.mlsdev.recipefinder.kotlinversion.R
import com.mlsdev.recipefinder.kotlinversion.data.entity.recipe.Ingredient
import com.mlsdev.recipefinder.kotlinversion.data.entity.recipe.Recipe
import com.mlsdev.recipefinder.kotlinversion.data.source.BaseObserver
import com.mlsdev.recipefinder.kotlinversion.data.source.repository.DataRepository
import com.mlsdev.recipefinder.kotlinversion.view.Extras
import com.mlsdev.recipefinder.kotlinversion.view.utils.UtilsUI
import com.mlsdev.recipefinder.kotlinversion.view.viewmodel.BaseViewModel
import javax.inject.Inject

class RecipeViewModel @Inject
constructor(context: Context, override var repository: DataRepository) : BaseViewModel(context) {
    private var recipe: Recipe? = null
    val recipeTitle = ObservableField<String>()
    val recipeImageUrl = ObservableField<String>("")
    val recipeHealthLabels = ObservableField<String>()
    val recipeDietLabels = ObservableField<String>()
    val recipeIngredients = ObservableField<String>()
    val favoriteImageStateChecked = ObservableBoolean(false)
    val proteinProgressValue = ObservableInt()
    val carbsProgressValue = ObservableInt()
    val fatProgressValue = ObservableInt()

    fun setRecipeData(recipeData: Bundle?) {
        if (recipeData != null && recipeData.containsKey(Extras.DATA)) {
            recipe = recipeData.getParcelable<Recipe>(Extras.DATA) as Recipe
            if (recipe != null) {
                recipeTitle.set(recipe!!.label)
                recipeImageUrl.set(if (!recipeData.containsKey(Extras.IMAGE_DATA)) recipe!!.image else "")
                recipeHealthLabels.set(getLabelsAsString(recipe!!.healthLabels))
                recipeDietLabels.set(getLabelsAsString(recipe!!.dietLabels))
                recipeIngredients.set(getIngredientsAsString(recipe!!.ingredients))

                setUpNutrients()
            }
        }
    }

    private fun setUpNutrients() {
        val totalWeight = recipe!!.totalWeight
        val proteinValue: Double = if (recipe!!.totalNutrients.protein != null) recipe!!.totalNutrients.protein!!.quantity else 0.0
        val carbsValue: Double = if (recipe!!.totalNutrients.carbs != null) recipe!!.totalNutrients.carbs!!.quantity else 0.0
        val fatValue: Double = if (recipe!!.totalNutrients.fat != null) recipe!!.totalNutrients.fat!!.quantity else 0.0

        proteinProgressValue.set(UtilsUI.Companion.getPercents(totalWeight, proteinValue))
        carbsProgressValue.set(UtilsUI.Companion.getPercents(totalWeight, carbsValue))
        fatProgressValue.set(UtilsUI.Companion.getPercents(totalWeight, fatValue))
    }

    override fun onStart() {
        checkIsTheRecipeInFavorites()
    }

    fun getIngredientsAsString(ingredients: Collection<Ingredient>): String {
        var ingredientsAsString = ""

        for (ingredient in ingredients) {
            if (!ingredientsAsString.isEmpty())
                ingredientsAsString = ingredientsAsString.plus(",\n")

            ingredientsAsString = ingredientsAsString.plus(context.getString(
                    R.string.ingredient_item,
                    ingredient.text,
                    UtilsUI.Companion.formatDecimalToString(ingredient.weight)))
        }

        return ingredientsAsString
    }

    fun getLabelsAsString(labels: List<String>): String {
        var labelsAsString = ""

        for (label in labels) {
            if (!labelsAsString.isEmpty())
                labelsAsString = labelsAsString.plus(", ")

            labelsAsString = labelsAsString.plus(label)
        }

        return labelsAsString
    }

    fun onFavoriteButtonClick(view: View?) {

        if (favoriteImageStateChecked.get()) {
            repository.removeFromFavorites(recipe!!)
                    .subscribe({ favoriteImageStateChecked.set(false) })
        } else {
            repository.addToFavorites(recipe!!)
                    .subscribe({ favoriteImageStateChecked.set(true) })
        }
    }

    fun checkIsTheRecipeInFavorites() {
        repository.isInFavorites(recipe!!)
                .subscribe(object : BaseObserver<Boolean>() {
                    override fun onSuccess(exist: Boolean) {
                        favoriteImageStateChecked.set(exist)
                    }
                })
    }
}
