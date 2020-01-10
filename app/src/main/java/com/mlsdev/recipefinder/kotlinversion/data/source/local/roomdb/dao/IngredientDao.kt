package com.mlsdev.recipefinder.kotlinversion.data.source.local.roomdb.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mlsdev.recipefinder.kotlinversion.data.entity.recipe.Ingredient

@Dao
interface IngredientDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(ingredients: List<Ingredient>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(ingredient: Ingredient)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun createIfNotExist(ingredient: Ingredient)

    @Query("SELECT * FROM ingredients WHERE recipe_uri = :arg0")
    fun loadByRecipeUri(arg0: String): List<Ingredient>

    @Query("DELETE FROM ingredients WHERE recipe_uri = :arg0")
    fun deleteByRecipeUri(arg0: String)
}
