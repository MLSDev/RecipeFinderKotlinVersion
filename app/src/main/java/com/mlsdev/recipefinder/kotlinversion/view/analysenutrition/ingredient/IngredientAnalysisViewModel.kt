package com.mlsdev.recipefinder.kotlinversion.view.analysenutrition.ingredient

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.support.v4.util.ArrayMap
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.mlsdev.recipefinder.kotlinversion.R
import com.mlsdev.recipefinder.kotlinversion.data.entity.nutrition.NutritionAnalysisResult
import com.mlsdev.recipefinder.kotlinversion.data.entity.nutrition.TotalNutrients
import com.mlsdev.recipefinder.kotlinversion.data.source.remote.ParameterKeys
import com.mlsdev.recipefinder.kotlinversion.data.source.repository.DataRepository
import com.mlsdev.recipefinder.kotlinversion.view.OnKeyboardStateChangedListener
import com.mlsdev.recipefinder.kotlinversion.view.listener.OnIngredientAnalyzedListener
import com.mlsdev.recipefinder.kotlinversion.view.utils.DiagramUtils
import com.mlsdev.recipefinder.kotlinversion.view.viewmodel.BaseViewModel
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class IngredientAnalysisViewModel @Inject
constructor(context: Context, diagramUtils: DiagramUtils, override var repository: DataRepository) : BaseViewModel(context), LifecycleObserver {
    private lateinit var onIngredientAnalyzedListener: OnIngredientAnalyzedListener
    private lateinit var keyboardListener: OnKeyboardStateChangedListener
    val ingredientText = ObservableField<String>("")
    val nutrientText = ObservableField<String>("")
    val fatText = ObservableField<String>("")
    val proteinText = ObservableField<String>("")
    val carbsText = ObservableField<String>("")
    val energyText = ObservableField<String>("")
    val diagramVisibility = ObservableInt(View.GONE)
    val energyLabelVisibility = ObservableInt(View.GONE)
    val fatLabelVisibility = ObservableInt(View.GONE)
    val carbsLabelVisibility = ObservableInt(View.GONE)
    val proteinLabelVisibility = ObservableInt(View.GONE)
    val analysisResultsWrapperVisibility = ObservableInt(View.INVISIBLE)
    val ingredientTextFocused = ObservableBoolean(false)
    private var totalNutrients: TotalNutrients ? = null
    private val diagramUtils = diagramUtils

    fun setKeyboardListener(keyboardListener: OnKeyboardStateChangedListener) {
        this.keyboardListener = keyboardListener
    }

    fun setOnIngredientAnalyzedListener(onIngredientAnalyzedListener: OnIngredientAnalyzedListener) {
        this.onIngredientAnalyzedListener = onIngredientAnalyzedListener
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun start() {
        prepareDiagramData(totalNutrients)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        Log.d("RF", "lifecycle stop")
    }

    fun onAnalyzeButtonClick(view: View?) {
        keyboardListener.hideKeyboard()

        if (ingredientText.get().isEmpty()) {
            val listener = View.OnClickListener {
                ingredientTextFocused.set(true)
                keyboardListener.showKeyboard()
            }

            if (actionListener != null)
                actionListener!!.showSnackbar(R.string.error_empty_ingredient_field, R.string.btn_fill_in, listener)
            return
        }

        if (actionListener != null)
            actionListener!!.showProgressDialog(true, "Analysing...")
        subscriptions.clear()

        val params: ArrayMap<String, String> = ArrayMap()
        params.put(ParameterKeys.INGREDIENT, ingredientText.get())
        repository.getIngredientData(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : SingleObserver<NutritionAnalysisResult> {

                    override fun onSubscribe(d: Disposable) {
                        subscriptions.add(d)
                    }

                    override fun onSuccess(result: NutritionAnalysisResult) {
                        if (actionListener != null)
                            actionListener!!.showProgressDialog(false, null)

                        val nutrients = result.totalNutrients
                        analysisResultsWrapperVisibility.set(View.VISIBLE)

                        nutrientText.set(ingredientText.get())
                        fatText.set(if (nutrients.fat != null) result.totalNutrients.fat!!.getFormattedFullText() else "")
                        proteinText.set(if (nutrients.protein != null) result.totalNutrients.protein!!.getFormattedFullText() else "")
                        carbsText.set(if (nutrients.carbs != null) result.totalNutrients.carbs!!.getFormattedFullText() else "")
                        energyText.set(if (nutrients.energy != null) result.totalNutrients.energy!!.getFormattedFullText() else "")

                        carbsLabelVisibility.set(if (carbsText.get().isEmpty()) View.GONE else View.VISIBLE)
                        proteinLabelVisibility.set(if (proteinText.get().isEmpty()) View.GONE else View.VISIBLE)
                        fatLabelVisibility.set(if (fatText.get().isEmpty()) View.GONE else View.VISIBLE)
                        energyLabelVisibility.set(if (energyText.get().isEmpty()) View.GONE else View.VISIBLE)

                        totalNutrients = nutrients
                        prepareDiagramData(totalNutrients)
                    }

                    override fun onError(e: Throwable) {
                        showError(e)
                    }

                })

    }

    fun onEditorAction(textView: TextView, actionId: Int, keyEvent: KeyEvent): Boolean {

        return if (actionId == KeyEvent.ACTION_DOWN || actionId == EditorInfo.IME_ACTION_DONE) {
            onAnalyzeButtonClick(null)
            keyboardListener.hideKeyboard()
            true
        } else {
            false
        }
    }

    private fun prepareDiagramData(nutrients: TotalNutrients?) {
        if (nutrients == null)
            return

        val entries = diagramUtils.preparePieEntries(nutrients)
        diagramVisibility.set(if (entries.isEmpty()) View.GONE else View.VISIBLE)
        val pieDataSet = diagramUtils.createPieDataSet(entries, "Nutrients", null)
        val pieData = diagramUtils.createPieData(pieDataSet)

        onIngredientAnalyzedListener.onIngredientAnalyzed(pieData)
        diagramVisibility.set(View.VISIBLE)
    }
}
