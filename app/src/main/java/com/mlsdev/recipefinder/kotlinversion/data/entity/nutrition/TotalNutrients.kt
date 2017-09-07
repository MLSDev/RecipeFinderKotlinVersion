package com.mlsdev.recipefinder.kotlinversion.data.entity.nutrition

import android.arch.persistence.room.*
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Entity(tableName = "total_nutrients",
        foreignKeys = arrayOf(
                ForeignKey(
                        entity = Nutrient::class,
                        parentColumns = arrayOf("id"),
                        childColumns = arrayOf("energy_id")),
                ForeignKey(
                        entity = Nutrient::class,
                        parentColumns = arrayOf("id"),
                        childColumns = arrayOf("fat_id")),
                ForeignKey(
                        entity = Nutrient::class,
                        parentColumns = arrayOf("id"),
                        childColumns = arrayOf("carbs_id")),
                ForeignKey(
                        entity = Nutrient::class,
                        parentColumns = arrayOf("id"),
                        childColumns = arrayOf("protein_id"))),
        indices = arrayOf(
                Index("energy_id"),
                Index("fat_id"),
                Index("carbs_id"),
                Index("protein_id"))
)
class TotalNutrients() : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    @Ignore
    @SerializedName("ENERC_KCAL")
    var energy: Nutrient? = null

    @Ignore
    @SerializedName("FAT")
    var fat: Nutrient? = null

    @Ignore
    @SerializedName("CHOCDF")
    var carbs: Nutrient? = null

    @Ignore
    @SerializedName("PROCNT")
    var protein: Nutrient? = null

    @ColumnInfo(name = "energy_id")
    var energyNutrientId: Long = 0

    @ColumnInfo(name = "fat_id")
    var fatNutrientId: Long = 0

    @ColumnInfo(name = "carbs_id")
    var carbsNutrientId: Long = 0

    @ColumnInfo(name = "protein_id")
    var proteinNutrientId: Long = 0

    constructor(parcel: Parcel) : this() {
        id = parcel.readLong()
        energy = parcel.readParcelable(Nutrient::class.java.classLoader)
        fat = parcel.readParcelable(Nutrient::class.java.classLoader)
        carbs = parcel.readParcelable(Nutrient::class.java.classLoader)
        protein = parcel.readParcelable(Nutrient::class.java.classLoader)
        energyNutrientId = parcel.readLong()
        fatNutrientId = parcel.readLong()
        carbsNutrientId = parcel.readLong()
        proteinNutrientId = parcel.readLong()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeParcelable(energy, flags)
        parcel.writeParcelable(fat, flags)
        parcel.writeParcelable(carbs, flags)
        parcel.writeParcelable(protein, flags)
        parcel.writeLong(energyNutrientId)
        parcel.writeLong(fatNutrientId)
        parcel.writeLong(carbsNutrientId)
        parcel.writeLong(proteinNutrientId)
    }

    override fun describeContents(): Int = 0

    companion object {
        @Suppress("unused")
        @JvmField
        val CREATOR: Parcelable.Creator<TotalNutrients> = object : Parcelable.Creator<TotalNutrients> {
            override fun createFromParcel(parcel: Parcel): TotalNutrients = TotalNutrients(parcel)
            override fun newArray(size: Int): Array<TotalNutrients?> = arrayOfNulls(size)
        }
    }
}
