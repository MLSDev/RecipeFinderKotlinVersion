package com.mlsdev.recipefinder.kotlinversion.data.entity.nutrition

data class RecipeAnalysisParams(
        var title: String,
        var prep: String,
        var yield: String,
        var ingr: List<String> = ArrayList())
