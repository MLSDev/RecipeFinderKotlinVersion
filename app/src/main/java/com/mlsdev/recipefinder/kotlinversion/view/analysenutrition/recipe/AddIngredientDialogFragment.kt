package com.mlsdev.recipefinder.kotlinversion.view.analysenutrition.recipe

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import com.mlsdev.recipefinder.kotlinversion.R
import com.mlsdev.recipefinder.kotlinversion.databinding.DialogFragmentAddIngredientBinding
import com.mlsdev.recipefinder.kotlinversion.view.BaseActivity

class AddIngredientDialogFragment : DialogFragment() {

    companion object {
        const val INGREDIENT_TITLE_KEY = "ingredient_label_key"
    }

    private lateinit var binding: DialogFragmentAddIngredientBinding


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DataBindingUtil
                .inflate(LayoutInflater.from(activity), R.layout.dialog_fragment_add_ingredient, null, false)

        (activity as BaseActivity).showSoftKeyboard()

        val builder = AlertDialog.Builder(activity, R.style.AlertDialogAppCompat)
        builder.setTitle("Add Ingredient")
                .setView(binding.root)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(R.string.btn_add) { _, _ ->
                    val data = Intent()
                    data.putExtra(INGREDIENT_TITLE_KEY, binding.etIngredientInput.text.toString())

                    if (targetFragment != null) {
                        targetFragment.onActivityResult(
                                RecipeAnalysisViewModel.ADD_INGREDIENT_REQUEST_CODE,
                                Activity.RESULT_OK,
                                data)
                    }

                    dismiss()
                }

        return builder.create()
    }

    override fun onDismiss(dialog: DialogInterface) {
        if (activity != null)
            (activity as BaseActivity).hideSoftKeyboard()
        super.onDismiss(dialog)
    }

}
