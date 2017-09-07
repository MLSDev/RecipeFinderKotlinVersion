package com.mlsdev.recipefinder.kotlinversion.data.entity.nutrition

import android.os.Parcel
import android.os.Parcelable

class NutritionAnalysisResult() : Parcelable {
    var uri: String = ""
    var calories: Int = 0
    var totalWeight: Double = 0.0
    var yields: Double = 0.0
    var dietLabels: List<String> = ArrayList()
    var healthLabels: List<String> = ArrayList()
    var cautions: List<String> = ArrayList()
    lateinit var totalNutrients: TotalNutrients

    constructor(parcel: Parcel) : this() {
        uri = parcel.readString()
        calories = parcel.readInt()
        totalWeight = parcel.readDouble()
        yields = parcel.readDouble()
        dietLabels = parcel.createStringArrayList()
        healthLabels = parcel.createStringArrayList()
        cautions = parcel.createStringArrayList()
        totalNutrients = parcel.readParcelable(TotalNutrients::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uri)
        parcel.writeInt(calories)
        parcel.writeDouble(totalWeight)
        parcel.writeDouble(yields)
        parcel.writeStringList(dietLabels)
        parcel.writeStringList(healthLabels)
        parcel.writeStringList(cautions)
        parcel.writeParcelable(totalNutrients, flags)
    }

    override fun describeContents(): Int = 0

    companion object {
        @Suppress("unused")
        @JvmField
        val CREATOR: Parcelable.Creator<NutritionAnalysisResult> = object : Parcelable.Creator<NutritionAnalysisResult> {
            override fun createFromParcel(parcel: Parcel): NutritionAnalysisResult = NutritionAnalysisResult(parcel)
            override fun newArray(size: Int): Array<NutritionAnalysisResult?> = arrayOfNulls(size)
        }
    }

}
