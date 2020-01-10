package com.mlsdev.recipefinder.kotlinversion.data.entity.recipe


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "ingredients")
class Ingredient : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    @ColumnInfo(name = "recipe_uri", index = true)
    var recipeUri: String = ""
    var text: String = ""
    var weight: Double = 0.0
}
