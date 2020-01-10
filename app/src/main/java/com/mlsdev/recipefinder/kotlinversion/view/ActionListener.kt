package com.mlsdev.recipefinder.kotlinversion.view

import androidx.annotation.StringRes
import android.view.View

interface ActionListener {

    fun onStartFilter()

    fun showProgressDialog(show: Boolean, message: String?)

    fun showSnackbar(@StringRes message: Int)

    fun showSnackbar(message: String)

    fun showSnackbar(message: String, action: String, listener: View.OnClickListener)

    fun showSnackbar(@StringRes message: Int, @StringRes action: Int, listener: View.OnClickListener?)

}