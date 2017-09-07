package com.mlsdev.recipefinder.kotlinversion.data.source;

import android.util.Log;

import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

abstract class BaseObserver<T> : SingleObserver<T> {

    companion object {
        const val SERVER_ERROR = 500
    }

    override fun onSubscribe(@NonNull d: Disposable) {
    }

    override fun onSuccess(@NonNull t: T) {
    }

    override fun onError(e: Throwable) {
        Log.e("", e.message)
    }

}
