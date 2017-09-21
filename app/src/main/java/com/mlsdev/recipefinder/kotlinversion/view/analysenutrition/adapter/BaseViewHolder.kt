package com.mlsdev.recipefinder.kotlinversion.view.analysenutrition.adapter

import android.support.v7.widget.RecyclerView
import android.view.View

abstract class BaseViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    abstract fun bindViewModel()
}
