package com.mlsdev.recipefinder.kotlinversion.data.entity.recipe;

import com.google.gson.annotations.SerializedName;

data class Params(
        @SerializedName("sane")
        val sane: ArrayList<String> = ArrayList(),
        @SerializedName("to")
        val to: List<String> = ArrayList(),
        @SerializedName("from")
        val from: List<String> = ArrayList(),
        @SerializedName("q")
        val queries: List<String> = ArrayList(),
        @SerializedName("calories")
        val calories: List<String> = ArrayList(),
        @SerializedName("health")
        val health: List<String> = ArrayList())
