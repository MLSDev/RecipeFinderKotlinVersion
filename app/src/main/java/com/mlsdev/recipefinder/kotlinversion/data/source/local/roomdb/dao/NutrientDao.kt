package com.mlsdev.recipefinder.kotlinversion.data.source.local.roomdb.dao

import androidx.room.*
import com.mlsdev.recipefinder.kotlinversion.data.entity.nutrition.Nutrient

@Dao
interface NutrientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(nutrients: List<Nutrient>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun createIfNotExist(nutrient: Nutrient?): Long

    @Query("SELECT * FROM nutrients WHERE id = :arg0")
    fun loadById(arg0: Long): Nutrient

    @Query("SELECT * FROM nutrients WHERE id in (:arg0)")
    fun loadByIds(arg0: List<Int>): List<Nutrient>

    @Query("delete FROM nutrients WHERE id = :arg0")
    fun deleteById(arg0: Long)

    @Delete
    fun delete(nutrient: Nutrient)

    @Delete
    fun delete(nutrients: List<Nutrient>)

    @Query("DELETE FROM nutrients WHERE id in (:arg0)")
    fun deleteByIds(arg0: List<Int>)

}
