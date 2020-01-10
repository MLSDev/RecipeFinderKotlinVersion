package com.mlsdev.recipefinder.kotlinversion.view.viewmodel

import androidx.lifecycle.ViewModel
import android.content.Context
import com.mlsdev.recipefinder.kotlinversion.R
import com.mlsdev.recipefinder.kotlinversion.data.source.BaseObserver
import com.mlsdev.recipefinder.kotlinversion.data.source.repository.DataRepository
import com.mlsdev.recipefinder.kotlinversion.view.ActionListener
import io.reactivex.disposables.CompositeDisposable
import retrofit2.HttpException
import java.io.IOException

open class BaseViewModel(val context: Context) : ViewModel() {
    open lateinit var repository: DataRepository
    val subscriptions = CompositeDisposable()
    var actionListener: ActionListener? = null

    open fun onDestroy() {
        subscriptions.clear()
    }

    open fun onStop() {
    }

    open fun onStart() {
    }

    protected fun showError(throwable: Throwable) {
        if (actionListener != null)
            actionListener!!.showProgressDialog(false, "")

        var errorMessage = context.getString(R.string.error_message_common)

        if (throwable is HttpException) {
            if (throwable.code() >= BaseObserver.SERVER_ERROR)
                errorMessage = context.getString(R.string.error_message_technical)
        } else if (throwable is IOException) {
            errorMessage = context.getString(R.string.error_message_connection)
        }

        if (actionListener != null)
            actionListener!!.showSnackbar(errorMessage)
    }
}
