package com.mlsdev.recipefinder.kotlinversion.data.entity.nutrition

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import com.mlsdev.recipefinder.kotlinversion.view.utils.UtilsUI

@Entity(tableName = "nutrients")
class Nutrient() : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    var label: String = ""
    var quantity: Double = 0.0
    var unit = ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readLong()
        label = parcel.readString()
        quantity = parcel.readDouble()
        unit = parcel.readString()
    }

    @Ignore
    constructor(label: String, quantity: Double, unit: String) : this() {
        this.label = label
        this.quantity = quantity
        this.unit = unit
    }

    fun getFormattedFullText(): String = "$label ${UtilsUI.formatDecimalToString(quantity)} $unit"

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(label)
        parcel.writeDouble(quantity)
        parcel.writeString(unit)
    }

    override fun describeContents(): Int = 0

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Nutrient> = object : Parcelable.Creator<Nutrient> {
            override fun createFromParcel(parcel: Parcel): Nutrient = Nutrient(parcel)
            override fun newArray(size: Int): Array<Nutrient?> = arrayOfNulls(size)
        }
    }


}
