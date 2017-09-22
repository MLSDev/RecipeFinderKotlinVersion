package com.mlsdev.recipefinder.kotlinversion

import com.google.gson.Gson
import com.mlsdev.recipefinder.kotlinversion.data.entity.recipe.Recipe
import org.robolectric.RuntimeEnvironment
import java.io.IOException

fun getRecipe(): Recipe {
    val json = getJsonStringFromResources("recipe.json")
    return Gson().fromJson(json, Recipe::class.java)
}

fun getJsonStringFromResources(fileName: String): String? {
    val context = RuntimeEnvironment.application
    var json: String? = null
    try {
        val inputStream = context.classLoader.getResourceAsStream(fileName)
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        json = String(buffer)
    } catch (e: IOException) {
        e.printStackTrace()
    }

    return json
}