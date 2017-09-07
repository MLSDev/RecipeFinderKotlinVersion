package com.mlsdev.recipefinder.kotlinversion.view

import android.content.Intent

interface NavigationController {

    fun launchActivity(intent: Intent)

    fun launchActivityForResult(intent: Intent, requestCode: Int)

    fun finishCurrentActivity()

    fun finishWithResult(data: Intent)

    fun setActivityResult(data: Intent)

}