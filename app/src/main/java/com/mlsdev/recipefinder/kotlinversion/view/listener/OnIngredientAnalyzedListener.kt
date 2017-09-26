package com.mlsdev.recipefinder.kotlinversion.view.listener

import com.github.mikephil.charting.data.PieData

interface OnIngredientAnalyzedListener {
    fun onIngredientAnalyzed(diagramData: PieData?)
}
