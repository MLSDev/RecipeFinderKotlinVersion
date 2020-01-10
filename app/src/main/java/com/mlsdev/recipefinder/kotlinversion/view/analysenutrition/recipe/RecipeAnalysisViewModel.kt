package com.mlsdev.recipefinder.kotlinversion.view.analysenutrition.recipe

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import android.content.Context
import android.content.Intent
import androidx.databinding.ObservableField
import android.util.Log
import android.view.View
import com.mlsdev.recipefinder.kotlinversion.R
import com.mlsdev.recipefinder.kotlinversion.data.entity.nutrition.NutritionAnalysisResult
import com.mlsdev.recipefinder.kotlinversion.data.entity.nutrition.RecipeAnalysisParams
import com.mlsdev.recipefinder.kotlinversion.data.source.BaseObserver
import com.mlsdev.recipefinder.kotlinversion.data.source.repository.DataRepository
import com.mlsdev.recipefinder.kotlinversion.view.MainActivity
import com.mlsdev.recipefinder.kotlinversion.view.listener.OnDataLoadedListener
import com.mlsdev.recipefinder.kotlinversion.view.viewmodel.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RecipeAnalysisViewModel @Inject
constructor(context: Context, override var repository: DataRepository) : BaseViewModel(context), LifecycleObserver {

    companion object {
        const val ADD_INGREDIENT_REQUEST_CODE = 0
    }

    val title = ObservableField<String>()
    val preparation = ObservableField<String>()
    val yields = ObservableField<String>()
    private val ingredients = ArrayList<String>()
    private var addIngredientListener: OnAddIngredientClickListener? = null
    private var dataLoadedListener: OnDataLoadedListener<List<String>>? = null


    fun setDataLoadedListener(dataLoadedListener: OnDataLoadedListener<List<String>>) {
        this.dataLoadedListener = dataLoadedListener
    }

    fun setAddIngredientListener(addIngredientListener: OnAddIngredientClickListener) {
        this.addIngredientListener = addIngredientListener
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun start() {
        if (dataLoadedListener != null)
            dataLoadedListener!!.onDataLoaded(ingredients)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        Log.d("RF", "lifecycle stop")
    }

    fun onAnalyzeButtonClick(view: View) {
        if (ingredients.isEmpty()) {
            val listener: View.OnClickListener = View.OnClickListener { addIngredientListener!!.onAddIngredientButtonClick() }
            actionListener!!.showSnackbar(R.string.no_ingredients_error_message, R.string.btn_add, listener)
            return
        }

        actionListener!!.showProgressDialog(true, "Analysing...")

        val recipeAnalysisParams = RecipeAnalysisParams(
                title.get(),
                preparation.get(),
                yields.get(),
                ingredients)

        subscriptions.clear()

        repository.getRecipeAnalysisData(recipeAnalysisParams)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : BaseObserver<NutritionAnalysisResult>() {
                    override fun onSuccess(nutritionAnalysisResult: NutritionAnalysisResult) {
                        actionListener!!.showProgressDialog(false, null)
                        Log.d(MainActivity.LOG_TAG, "onNext()")
                        val intent = Intent(context, RecipeAnalysisDetailsActivity::class.java)
                        intent.putExtra(RecipeAnalysisDetailsActivity.RECIPE_ANALYSING_RESULT_KEY, nutritionAnalysisResult)
                        context.startActivity(intent)
                    }

                    override fun onSubscribe(d: Disposable) {
                        subscriptions.add(d)
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        showError(e)
                    }
                })

    }

    fun setIngredients(ingredients: List<String>) {
        this.ingredients.clear()
        this.ingredients.addAll(ingredients)
    }

}
