package com.mlsdev.recipefinder.kotlinversion.view

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.support.annotation.NonNull
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.view.MenuItem
import com.mlsdev.recipefinder.kotlinversion.R
import com.mlsdev.recipefinder.kotlinversion.view.enums.TabItemType.*
import com.mlsdev.recipefinder.kotlinversion.view.fragment.TabFragment
import javax.inject.Inject

class BottomNavigationItemSelectedListener @Inject
constructor() : BottomNavigationView.OnNavigationItemSelectedListener, LifecycleObserver {
    lateinit var fragmentManager: FragmentManager
    lateinit var currentMenuItem: MenuItem
    private var analyseNutritionFragment: TabFragment = TabFragment.getNewInstance(ANALYSE)
    private var searchRecipesFragment: TabFragment = TabFragment.getNewInstance(SEARCH)
    private var favoriteRecipesFragment: TabFragment = TabFragment.getNewInstance(FAVORITES)
    private lateinit var currentFragment: TabFragment
    var checkedItemId: Int = -1

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        onNavigationItemSelected(currentMenuItem)
    }

    override fun onNavigationItemSelected(@NonNull item: MenuItem): Boolean {
        currentMenuItem = item

        if (checkedItemId == item.itemId) {
            currentFragment.scrollToTop()
            return true
        }

        checkedItemId = item.itemId

        return when (checkedItemId) {
            R.id.action_search_recipe -> {
                replaceFragment(searchRecipesFragment)
                true
            }
            R.id.action_analyse_nutrition -> {
                replaceFragment(analyseNutritionFragment)
                true
            }
            R.id.action_favorites -> {
                replaceFragment(favoriteRecipesFragment)
                true
            }
            else -> {
                false
            }
        }

    }

    private fun replaceFragment(fragment: TabFragment) {
        currentFragment = fragment

        clearBackStack()

        fragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.fl_content, fragment)
                .commit()
    }

    private fun clearBackStack() {
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStackImmediate()
            clearBackStack()
        }
    }

}
