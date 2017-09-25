
import com.google.gson.Gson
import com.mlsdev.recipefinder.kotlinversion.data.entity.recipe.Recipe
import com.mlsdev.recipefinder.kotlinversion.data.entity.recipe.SearchResult
import org.robolectric.RuntimeEnvironment
import java.io.IOException

fun getRecipe(): Recipe {
    val json = getJsonStringFromResources("recipe.json")
    return Gson().fromJson(json, Recipe::class.java)
}

fun getSearchResult(): List<Recipe> {
    val json = getJsonStringFromResources("search_result.json")
    val searchResult = Gson().fromJson(json, SearchResult::class.java)
    return searchResult.hits.mapTo(ArrayList(), { it.recipe })
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