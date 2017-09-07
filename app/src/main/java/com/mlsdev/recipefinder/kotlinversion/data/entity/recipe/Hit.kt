package com.mlsdev.recipefinder.kotlinversion.data.entity.recipe

import com.google.gson.annotations.SerializedName

data class Hit(
        @SerializedName("recipe") val recipe: Recipe,
        @SerializedName("bookmarked") val bookmarked: Boolean,
        @SerializedName("bought") val bought: Boolean
)