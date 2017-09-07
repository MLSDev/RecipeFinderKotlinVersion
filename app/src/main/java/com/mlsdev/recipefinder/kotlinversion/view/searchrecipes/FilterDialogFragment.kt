package com.mlsdev.recipefinder.kotlinversion.view.searchrecipes

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog

import com.mlsdev.recipefinder.kotlinversion.R
import com.mlsdev.recipefinder.kotlinversion.databinding.DialogFragmentSearchFilterBinding

class FilterDialogFragment : DialogFragment() {

    companion object {
        const val HEALTH_LABEL_KEY = "health_label_key"
        const val DIET_LABEL_KEY = "diet_label_key"
    }

    private lateinit var binding: DialogFragmentSearchFilterBinding


    private var healthSelectedIndex = 0
    private var dietSelectedIndex = 0

    override fun onStart() {
        super.onStart()
        binding.spHealthLabels.setSelection(healthSelectedIndex)
        binding.spDietLabels.setSelection(dietSelectedIndex)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = activity.layoutInflater
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_search_filter, null, false)

        val builder = AlertDialog.Builder(activity, R.style.AlertDialogAppCompat)
        builder.setView(binding.root)
                .setTitle(R.string.dialog_title_filter)
                .setPositiveButton(R.string.btn_apply_filter, { _, _ ->
                    val data = Intent()
                    healthSelectedIndex = binding.spHealthLabels.selectedItemPosition
                    dietSelectedIndex = binding.spDietLabels.selectedItemPosition
                    data.putExtra(HEALTH_LABEL_KEY, binding.spHealthLabels.selectedItem.toString())
                    data.putExtra(DIET_LABEL_KEY, binding.spDietLabels.selectedItem.toString())

                    if (targetFragment != null) {
                        targetFragment.onActivityResult(SearchRecipeFragment.FILTER_REQUEST_CODE,
                                Activity.RESULT_OK, data)
                    }

                    dismiss()
                })


        return builder.create()
    }
}
