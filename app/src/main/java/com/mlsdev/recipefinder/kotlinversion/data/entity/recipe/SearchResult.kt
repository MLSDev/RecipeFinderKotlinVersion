package com.mlsdev.recipefinder.kotlinversion.data.entity.recipe;

import com.google.gson.annotations.SerializedName

data class SearchResult(
        @SerializedName("q")
        val query: String,
        @SerializedName("from")
        val from: Int,
        @SerializedName("to")
        val to: Int,
        @SerializedName("params")
        val params: Params,
        @SerializedName("more")
        val more: Boolean,
        @SerializedName("count")
        val count: Int,
        @SerializedName("hits")
        var hits: List<Hit> = ArrayList())
