package com.mlsdev.recipefinder.kotlinversion.data.source.local.roomdb.dao

import androidx.room.*
import com.mlsdev.recipefinder.kotlinversion.data.entity.nutrition.TotalNutrients

@Dao
interface TotalNutrientsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createIfNotExist(totalNutrients: TotalNutrients): Long

    @Query("SELECT * FROM total_nutrients WHERE id = :arg0")
    fun loadById(arg0: Long): TotalNutrients

    @Query("DELETE FROM total_nutrients WHERE id = :arg0")
    fun deleteById(arg0: Long)

    @Delete
    fun delete(totalNutrients: TotalNutrients)
}
