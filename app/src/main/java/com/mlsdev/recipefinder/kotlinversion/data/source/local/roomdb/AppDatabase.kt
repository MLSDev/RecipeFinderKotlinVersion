package com.mlsdev.recipefinder.kotlinversion.data.source.local.roomdb

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.mlsdev.recipefinder.kotlinversion.data.entity.nutrition.Nutrient
import com.mlsdev.recipefinder.kotlinversion.data.entity.nutrition.TotalNutrients
import com.mlsdev.recipefinder.kotlinversion.data.entity.recipe.Ingredient
import com.mlsdev.recipefinder.kotlinversion.data.entity.recipe.Recipe
import com.mlsdev.recipefinder.kotlinversion.data.source.local.roomdb.dao.IngredientDao
import com.mlsdev.recipefinder.kotlinversion.data.source.local.roomdb.dao.NutrientDao
import com.mlsdev.recipefinder.kotlinversion.data.source.local.roomdb.dao.RecipeDao
import com.mlsdev.recipefinder.kotlinversion.data.source.local.roomdb.dao.TotalNutrientsDao

@Database(
        entities = arrayOf(
                Recipe::class,
                TotalNutrients::class,
                Nutrient::class,
                Ingredient::class),
        version = 1,
        exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun recipeDao(): RecipeDao

    abstract fun nutrientDao(): NutrientDao

    abstract fun totalNutrientsDao(): TotalNutrientsDao

    abstract fun ingredientDao(): IngredientDao

}
