package com.mlsdev.recipefinder.kotlinversion.view.message

import android.app.ProgressDialog
import com.mlsdev.recipefinder.kotlinversion.R
import com.mlsdev.recipefinder.kotlinversion.view.BaseActivity

class ProgressDialogMessage(activity: BaseActivity) : Message(activity) {
    private var progressDialog: ProgressDialog? = null

    fun showProgressDialog(isShow: Boolean, message: String?) {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(activity, R.style.AlertDialogAppCompat)
            progressDialog!!.isIndeterminate = true
            progressDialog!!.setMessage(message)
        }

        if (isShow)
            progressDialog!!.show()
        else
            progressDialog!!.dismiss()
    }
}
