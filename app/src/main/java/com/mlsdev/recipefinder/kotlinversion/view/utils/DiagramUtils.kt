package com.mlsdev.recipefinder.kotlinversion.view.utils

import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.mlsdev.recipefinder.kotlinversion.R
import com.mlsdev.recipefinder.kotlinversion.data.entity.nutrition.TotalNutrients

open class DiagramUtils(private val utilsUI: UtilsUI) {

    fun preparePieEntries(nutrients: TotalNutrients): ArrayList<PieEntry> {
        val entries: ArrayList<PieEntry> = ArrayList(3)
        if (nutrients.protein != null)
            entries.add(PieEntry(nutrients.protein!!.quantity.toFloat(), nutrients.protein!!.label))
        if (nutrients.fat != null)
            entries.add(PieEntry(nutrients.fat!!.quantity.toFloat(), nutrients.fat!!.label))
        if (nutrients.fat != null)
            entries.add(PieEntry(nutrients.carbs!!.quantity.toFloat(), nutrients.carbs!!.label))

        return entries
    }

    fun preparePieChart(pieChart: PieChart): PieChart {
        pieChart.setUsePercentValues(true)
        pieChart.description.isEnabled = false
        pieChart.setExtraOffsets(5F, 10F, 5F, 5F)
        pieChart.dragDecelerationFrictionCoef = 0.96f

        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(android.R.color.transparent)

        pieChart.setTransparentCircleColor(utilsUI.getColor(android.R.color.white))
        pieChart.setTransparentCircleAlpha(50)

        pieChart.holeRadius = 40f
        pieChart.transparentCircleRadius = 45f

        pieChart.setDrawCenterText(true)
        pieChart.centerText = "Balance"

        pieChart.rotationAngle = 0F
        // enable rotation of the chart by touch
        pieChart.isRotationEnabled = false
        pieChart.isHighlightPerTapEnabled = true
        pieChart.setDrawEntryLabels(true)
        pieChart.setEntryLabelColor(utilsUI.getColor(android.R.color.white))
        pieChart.setEntryLabelTextSize(15f)

        val l = pieChart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(false)

        return pieChart
    }

    fun createPieDataSet(pieEntryList: List<PieEntry>, label: String?, colors: List<Int>?): PieDataSet {
        val pieDataSet = PieDataSet(pieEntryList, label)
        pieDataSet.sliceSpace = 1.5f
        pieDataSet.selectionShift = 2f

        pieDataSet.setDrawValues(true)
        val newColors = ArrayList<Int>(3)

        if (colors == null) {
            newColors.add(utilsUI.getColor(R.color.colorPrimaryDark))
            newColors.add(utilsUI.getColor(R.color.colorPrimary))
            newColors.add(utilsUI.getColor(R.color.colorAccent))
        }

        pieDataSet.colors = colors ?: newColors
        return pieDataSet
    }

    fun createPieData(pieDataSet: PieDataSet): PieData {
        val pieData = PieData(pieDataSet)
        pieData.setValueFormatter(PercentFormatter())
        pieData.setValueTextSize(15f)
        pieData.setValueTextColor(utilsUI.getColor(android.R.color.white))
        return pieData
    }

    fun setData(pieChart: PieChart, pieData: PieData) {
        pieChart.data = pieData
        pieChart.highlightValue(null)
        pieChart.invalidate()
        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad)
    }

}
