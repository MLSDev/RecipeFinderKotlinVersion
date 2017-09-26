package com.mlsdev.recipefinder.kotlinversion.view.fragment

import android.arch.lifecycle.LifecycleFragment
import android.support.annotation.StringRes
import android.view.View
import com.mlsdev.recipefinder.kotlinversion.view.ActionListener
import com.mlsdev.recipefinder.kotlinversion.view.BaseActivity

abstract class BaseFragment : LifecycleFragment(), ActionListener {

    override fun onStartFilter() {
    }

    override fun showProgressDialog(show: Boolean, message: String?) {
        (activity as BaseActivity).showProgressDialog(show, message)
    }

    override fun showSnackbar(@StringRes message: Int) {
        (activity as BaseActivity).showSnackbar(message)
    }

    override fun showSnackbar(message: String) {
        (activity as BaseActivity).showSnackbar(message)
    }

    override fun showSnackbar(message: String, action: String, listener: View.OnClickListener) {
        (activity as BaseActivity).showSnackbar(message, action, listener)
    }

    override fun showSnackbar(@StringRes message: Int, @StringRes action: Int, listener: View.OnClickListener?) {
        (activity as BaseActivity).showSnackbar(message, action, listener)
    }
}
