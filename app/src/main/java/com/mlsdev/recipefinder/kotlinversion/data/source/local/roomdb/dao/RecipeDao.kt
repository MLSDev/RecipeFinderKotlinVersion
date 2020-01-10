package com.mlsdev.recipefinder.kotlinversion.data.source.local.roomdb.dao

import androidx.room.*
import com.mlsdev.recipefinder.kotlinversion.data.entity.recipe.Recipe
import io.reactivex.Flowable

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(recipe: Recipe): Long

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT uri FROM recipes WHERE uri = :arg0")
    fun loadByUri(arg0: String): Flowable<Recipe>

    @Query("SELECT * FROM recipes")
    fun loadAll(): Flowable<List<Recipe>>

    @Delete
    fun delete(recipe: Recipe): Int

    @Delete
    fun delete(recipes: List<Recipe>)

    @Query("DELETE FROM recipes WHERE uri = :arg0")
    fun deleteByIds(arg0: String)

}
