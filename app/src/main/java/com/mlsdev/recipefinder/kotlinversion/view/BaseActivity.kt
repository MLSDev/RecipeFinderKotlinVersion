package com.mlsdev.recipefinder.kotlinversion.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.mlsdev.recipefinder.kotlinversion.view.message.ProgressDialogMessage
import com.mlsdev.recipefinder.kotlinversion.view.message.SnackbarMessage
import dagger.android.support.DaggerAppCompatActivity

@SuppressLint("Registered")
open class BaseActivity : DaggerAppCompatActivity(), NavigationController, ActionListener {
    private lateinit var progressDialogMessage: ProgressDialogMessage
    private lateinit var snackbarMessage: SnackbarMessage

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressDialogMessage = ProgressDialogMessage(this)
        snackbarMessage = SnackbarMessage(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    fun hideSoftKeyboard() {
        val view: View? = currentFocus

        if (view != null) {
            val inputMethodManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromInputMethod(view.windowToken, 0)
        }
    }

    fun showSoftKeyboard() {
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    override fun launchActivity(intent: Intent) {
        startActivity(intent)
    }

    override fun launchActivityForResult(intent: Intent, requestCode: Int) {
        startActivityForResult(intent, requestCode)
    }

    override fun finishCurrentActivity() {
        finish()
    }

    override fun finishWithResult(data: Intent) {
        setActivityResult(data)
        finish()
    }

    override fun setActivityResult(data: Intent) {
        setResult(Activity.RESULT_OK, data)
    }

    override fun onStartFilter() {
    }

    override fun showProgressDialog(show: Boolean, message: String?) {
        progressDialogMessage.showProgressDialog(show, message)
    }

    override fun showSnackbar(@StringRes message: Int) {
        snackbarMessage.showSnackbar(message)
    }

    override fun showSnackbar(message: String) {
        snackbarMessage.showSnackbar(message)
    }

    override fun showSnackbar(message: String, action: String, listener: View.OnClickListener) {
        snackbarMessage.showSnackbar(message, action, listener)
    }

    override fun showSnackbar(@StringRes message: Int, @StringRes action: Int, listener: View.OnClickListener?) {
        snackbarMessage.showSnackbar(message, action, listener)
    }
}
