package com.mlsdev.recipefinder.kotlinversion.view.analysenutrition

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mlsdev.recipefinder.kotlinversion.R
import com.mlsdev.recipefinder.kotlinversion.databinding.FragmentAnalyseNutritionBinding
import com.mlsdev.recipefinder.kotlinversion.view.analysenutrition.ingredient.IngredientAnalysisFragment
import com.mlsdev.recipefinder.kotlinversion.view.analysenutrition.recipe.RecipeAnalysisFragment
import com.mlsdev.recipefinder.kotlinversion.view.fragment.TabFragment

class AnalyseNutritionFragment : TabFragment() {

    companion object {
        const val INGREDIENT_ANALYSIS_FRAGMENT = 0
        const val RECIPE_ANALYSIS_FRAGMENT = 1
        const val PAGES_NUMBER = 2
    }

    private lateinit var binding: FragmentAnalyseNutritionBinding
    private lateinit var pagerAdapter: ViewPagerAdapter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_analyse_nutrition, container, false)
        initViewPager()
        return binding.root
    }

    private fun initViewPager() {
        pagerAdapter = ViewPagerAdapter(childFragmentManager)
        binding.vpAnalysisPages.adapter = pagerAdapter
        binding.vpAnalysisPages.offscreenPageLimit = PAGES_NUMBER
        binding.tlAnalysisTabs.setupWithViewPager(binding.vpAnalysisPages)
    }

    inner class ViewPagerAdapter(childFragmentManager: FragmentManager) : FragmentPagerAdapter(childFragmentManager) {

        private val nutritionAnalysisFragment = IngredientAnalysisFragment()
        private val recipeAnalysisFragment = RecipeAnalysisFragment()

        override fun getItem(position: Int): Fragment {
            return if (position == RECIPE_ANALYSIS_FRAGMENT)
                recipeAnalysisFragment
            else
                nutritionAnalysisFragment
        }

        override fun getCount(): Int = PAGES_NUMBER

        override fun getPageTitle(position: Int): CharSequence {
            return getString(
                    if (position == RECIPE_ANALYSIS_FRAGMENT)
                        R.string.tab_recipe_analysis
                    else
                        R.string.tab_nutrition_analysis)
        }
    }
}
