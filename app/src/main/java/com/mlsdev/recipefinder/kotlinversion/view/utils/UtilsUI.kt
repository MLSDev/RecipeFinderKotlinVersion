package com.mlsdev.recipefinder.kotlinversion.view.utils

import android.content.Context
import android.os.Build
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import java.text.DecimalFormat

class UtilsUI(val context: Context) {

    @ColorInt
    fun getColor(@ColorRes colorResId: Int): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.resources.getColor(colorResId, context.theme)
        } else {
            context.resources.getColor(colorResId)
        }
    }

    companion object {
        fun formatDecimalToString(value: Double): String {
            return DecimalFormat("#0.00").format(value)
        }

        fun getPercents(fullValue: Double, partValue: Double): Int {
            return ((partValue / fullValue) * 100).toInt()
        }
    }
}
