package com.mlsdev.recipefinder.kotlinversion.data.source.local

import com.mlsdev.recipefinder.kotlinversion.data.entity.nutrition.TotalNutrients
import com.mlsdev.recipefinder.kotlinversion.data.entity.recipe.Recipe
import com.mlsdev.recipefinder.kotlinversion.data.source.BaseDataSource
import com.mlsdev.recipefinder.kotlinversion.data.source.DataSource
import com.mlsdev.recipefinder.kotlinversion.data.source.local.roomdb.AppDatabase
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class LocalDataSource(val db: AppDatabase) : BaseDataSource(), DataSource {

    override fun getFavorites(): Flowable<List<Recipe>> {
        return db.recipeDao().loadAll()
                .map {
                    for (recipe in it) {
                        val totalNutrients: TotalNutrients = db.totalNutrientsDao().loadById(recipe.totalNutrientsId) ?: continue

                        totalNutrients.energy = db.nutrientDao().loadById(totalNutrients.energyNutrientId)
                        totalNutrients.fat = db.nutrientDao().loadById(totalNutrients.fatNutrientId)
                        totalNutrients.carbs = db.nutrientDao().loadById(totalNutrients.carbsNutrientId)
                        totalNutrients.protein = db.nutrientDao().loadById(totalNutrients.proteinNutrientId)

                        recipe.totalNutrients = totalNutrients
                        recipe.ingredients = db.ingredientDao().loadByRecipeUri(recipe.uri)
                    }

                    it
                }
    }

    override fun addToFavorites(recipe: Recipe): Completable {
        return Completable.fromCallable {
            recipe.totalNutrients.energyNutrientId = db.nutrientDao().createIfNotExist(recipe.totalNutrients.energy)
            recipe.totalNutrients.fatNutrientId = db.nutrientDao().createIfNotExist(recipe.totalNutrients.fat)
            recipe.totalNutrients.carbsNutrientId = db.nutrientDao().createIfNotExist(recipe.totalNutrients.carbs)
            recipe.totalNutrients.proteinNutrientId = db.nutrientDao().createIfNotExist(recipe.totalNutrients.protein)
            recipe.totalNutrientsId = db.totalNutrientsDao().createIfNotExist(recipe.totalNutrients)

            for (ingredient in recipe.ingredients)
                ingredient.recipeUri = recipe.uri

            db.ingredientDao().insert(recipe.ingredients)
            db.recipeDao().insert(recipe)
        }
    }

    override fun removeFromFavorites(recipe: Recipe): Completable {
        return Completable.fromCallable {
            db.recipeDao().delete(recipe)
            db.totalNutrientsDao().delete(recipe.totalNutrients)
            db.nutrientDao().delete(recipe.totalNutrients.energy!!)
            db.nutrientDao().delete(recipe.totalNutrients.fat!!)
            db.nutrientDao().delete(recipe.totalNutrients.carbs!!)
            db.nutrientDao().delete(recipe.totalNutrients.protein!!)
            db.ingredientDao().deleteByRecipeUri(recipe.uri)
        }
    }

    override fun isInFavorites(recipe: Recipe): Single<Boolean> {
        return db.recipeDao().loadByUri(recipe.uri)
                .map { true }
                .first(false)
    }

}
