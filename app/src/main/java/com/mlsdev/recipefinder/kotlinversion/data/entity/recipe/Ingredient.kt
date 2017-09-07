package com.mlsdev.recipefinder.kotlinversion.data.entity.recipe


import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
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
