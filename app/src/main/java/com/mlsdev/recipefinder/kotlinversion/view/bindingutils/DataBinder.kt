package com.mlsdev.recipefinder.kotlinversion.view.bindingutils

import android.animation.ObjectAnimator
import android.databinding.BindingAdapter
import android.support.design.widget.TextInputEditText
import android.support.v4.widget.ContentLoadingProgressBar
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.mlsdev.recipefinder.kotlinversion.R

class DataBinder {

    object Adapters {
        @JvmStatic
        @BindingAdapter("imageUrl")
        fun setImageUrl(imageView: ImageView, imageUrl: String) {
            if (imageUrl.isEmpty())
                return

            Glide.with(imageView.context)
                    .load(imageUrl)
                    .override(600, 400)
                    .error(R.mipmap.ic_launcher)
                    .into(imageView)
        }

        @JvmStatic
        @BindingAdapter("progressValue")
        fun setProgressValue(progressBar: ContentLoadingProgressBar, progressValue: Int) {
            val objectAnimator = ObjectAnimator.ofInt(progressBar, "progress", 0, progressValue)
            objectAnimator.duration = 350
            objectAnimator.startDelay = 250
            objectAnimator.start()
        }

        @JvmStatic
        @BindingAdapter("focused")
        fun setFocused(editText: TextInputEditText, requestFocus: Boolean) {
            if (requestFocus)
                editText.requestFocus()
        }

    }

}
