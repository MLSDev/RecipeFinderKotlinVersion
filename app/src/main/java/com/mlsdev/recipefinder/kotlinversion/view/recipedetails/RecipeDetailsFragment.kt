package com.mlsdev.recipefinder.kotlinversion.view.recipedetails

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.os.Bundle
import androidx.core.view.ViewCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mlsdev.recipefinder.kotlinversion.R
import com.mlsdev.recipefinder.kotlinversion.databinding.FragmentRecipeDetailsBinding
import com.mlsdev.recipefinder.kotlinversion.view.Extras
import com.mlsdev.recipefinder.kotlinversion.view.MainActivity
import com.mlsdev.recipefinder.kotlinversion.view.fragment.BaseFragment
import javax.inject.Inject

class RecipeDetailsFragment : BaseFragment() {
    private lateinit var binding: FragmentRecipeDetailsBinding
    private lateinit var viewModel: RecipeViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RecipeViewModel::class.java)
        viewModel.setRecipeData(arguments)
        viewModel.actionListener = this
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_details, container, false)
        binding.viewModel = viewModel

        (activity as MainActivity).setSupportActionBar(binding.toolbar)
        if ((activity as MainActivity).supportActionBar != null) {
            (activity as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        val imageTransitionName = arguments.getString(Extras.IMAGE_TRANSITION_NAME)

        if (imageTransitionName != null)
            ViewCompat.setTransitionName(binding.ivRecipeImage, imageTransitionName)

        val bitmap: Bitmap? = arguments.getParcelable(Extras.IMAGE_DATA)
        if (bitmap != null)
            binding.ivRecipeImage.setImageBitmap(bitmap)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }
}
