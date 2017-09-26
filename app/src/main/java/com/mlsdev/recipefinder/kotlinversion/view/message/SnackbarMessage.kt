package com.mlsdev.recipefinder.kotlinversion.view.message

import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.view.View
import com.mlsdev.recipefinder.kotlinversion.R
import com.mlsdev.recipefinder.kotlinversion.view.BaseActivity

class SnackbarMessage(activity: BaseActivity) : Message(activity) {

    fun showSnackbar(@StringRes message: Int) {
        showSnackbar(activity.getString(message))
    }

    fun showSnackbar(message: String) {
        showSnackbar(message, null, null)
    }

    fun showSnackbar(@StringRes message: Int, @StringRes action: Int, listener: View.OnClickListener?) {
        showSnackbar(activity.getString(message), activity.getString(action), listener)
    }

    fun showSnackbar(message: String, action: String?, listener: View.OnClickListener?) {
        val tag = activity.getString(R.string.content_tag)
        val view: View? = activity.window.decorView.findViewWithTag(tag)
        if (view != null) {
            val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)

            if (action != null && listener != null)
                snackbar.setAction(action, listener)

            snackbar.show()
        }

    }
}
