package com.mlsdev.recipefinder.kotlinversion.view.fragment

import android.support.annotation.NonNull
import com.mlsdev.recipefinder.kotlinversion.view.analysenutrition.AnalyseNutritionFragment
import com.mlsdev.recipefinder.kotlinversion.view.enums.TabItemType
import com.mlsdev.recipefinder.kotlinversion.view.favoriterecipes.FavoriteRecipesFragment
import com.mlsdev.recipefinder.kotlinversion.view.searchrecipes.SearchRecipeFragment

abstract class TabFragment : BaseFragment() {

    companion object {
        @NonNull
        fun getNewInstance(tabItemType: TabItemType): TabFragment {

            return when (tabItemType) {
                TabItemType.ANALYSE -> AnalyseNutritionFragment()
                TabItemType.FAVORITES -> FavoriteRecipesFragment()
                else -> SearchRecipeFragment()
            }

        }
    }


    /**
     * Scrolls the root view of the fragment to top by clicking on a current tab in the navigation view.
     */
    open fun scrollToTop() {
    }

}
