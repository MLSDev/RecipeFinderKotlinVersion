package com.mlsdev.recipefinder.kotlinversion.data.entity.recipe

import androidx.room.*
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.mlsdev.recipefinder.kotlinversion.data.entity.nutrition.TotalNutrients
import com.mlsdev.recipefinder.kotlinversion.data.source.local.roomdb.converter.Converter

@Entity(tableName = "recipes",
        foreignKeys = arrayOf(
                ForeignKey(
                        entity = TotalNutrients::class,
                        parentColumns = arrayOf("id"),
                        childColumns = arrayOf("total_nutrients_id"))),
        indices = arrayOf(Index("total_nutrients_id"))
)
@TypeConverters(Converter::class)
class Recipe() : Parcelable {
    @PrimaryKey
    var uri: String = ""
    var label: String = ""
    var image: String = ""
    var source: String = ""
    var yields: Double = 0.0
    var calories: Double = 0.0

    @SerializedName("totalWeight")
    var totalWeight: Double = 0.0

    @Ignore
    @SerializedName("totalNutrients")
    lateinit var totalNutrients: TotalNutrients

    @ColumnInfo(name = "total_nutrients_id")
    var totalNutrientsId: Long = 0

    @ColumnInfo(name = "diet_labels")
    @SerializedName("dietLabels")
    var dietLabels: List<String> = ArrayList()

    @ColumnInfo(name = "health_labels")
    @SerializedName("healthLabels")
    var healthLabels: List<String> = ArrayList()

    @Ignore
    @SerializedName("ingredients")
    var ingredients: List<Ingredient> = ArrayList()

    constructor(parcel: Parcel) : this() {
        uri = parcel.readString()
        label = parcel.readString()
        image = parcel.readString()
        source = parcel.readString()
        yields = parcel.readDouble()
        calories = parcel.readDouble()
        totalWeight = parcel.readDouble()
        totalNutrients = parcel.readParcelable(TotalNutrients::class.java.classLoader)
        totalNutrientsId = parcel.readLong()
        dietLabels = parcel.createStringArrayList()
        healthLabels = parcel.createStringArrayList()
    }

    @Ignore
    constructor(totalNutrientsId: Long) : this() {
        this.totalNutrientsId = totalNutrientsId;
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uri)
        parcel.writeString(label)
        parcel.writeString(image)
        parcel.writeString(source)
        parcel.writeDouble(yields)
        parcel.writeDouble(calories)
        parcel.writeDouble(totalWeight)
        parcel.writeParcelable(totalNutrients, flags)
        parcel.writeLong(totalNutrientsId)
        parcel.writeStringList(dietLabels)
        parcel.writeStringList(healthLabels)
    }

    override fun describeContents(): Int = 0

    companion object {
        @Suppress("unused")
        @JvmField
        val CREATOR: Parcelable.Creator<Recipe> = object : Parcelable.Creator<Recipe> {
            override fun createFromParcel(parcel: Parcel): Recipe = Recipe(parcel)
            override fun newArray(size: Int): Array<Recipe?> = arrayOfNulls(size)
        }
    }

}
