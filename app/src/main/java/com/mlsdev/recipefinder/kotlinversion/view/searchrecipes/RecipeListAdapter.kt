package com.mlsdev.recipefinder.kotlinversion.view.searchrecipes

import android.databinding.DataBindingUtil
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import com.mlsdev.recipefinder.kotlinversion.R
import com.mlsdev.recipefinder.kotlinversion.data.entity.recipe.Recipe
import com.mlsdev.recipefinder.kotlinversion.databinding.ProgressViewBinding
import com.mlsdev.recipefinder.kotlinversion.databinding.RecipeListItemBinding
import com.mlsdev.recipefinder.kotlinversion.view.analysenutrition.adapter.BaseViewHolder
import com.mlsdev.recipefinder.kotlinversion.view.analysenutrition.adapter.ProgressViewHolder

class RecipeListAdapter(
        val onLastItemShownListener: OnLastItemShownListener,
        val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<BaseViewHolder>() {

    companion object {
        const val ITEM = 0
        const val PROGRESS_VIEW = 1
    }

    private var isLoadMoreItems = true
    private val recipes = ArrayList<Recipe>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {

        if (viewType == ITEM) {
            val binding: RecipeListItemBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.recipe_list_item,
                    parent,
                    false)

            return RecipeViewHolder(binding)
        } else {
            val binding: ProgressViewBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.progress_view,
                    parent,
                    false)

            return ProgressViewHolder(binding.root)
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bindViewModel()

        // if 1 item before the end of the list
        if ((position == itemCount - 1) && isLoadMoreItems)
            onLastItemShownListener.onLastItemShown()
    }

    override fun getItemCount(): Int {
        var itemCount = recipes.size
        itemCount = if ((itemCount % 10 == 0) && (itemCount > 0) && isLoadMoreItems) itemCount + PROGRESS_VIEW else itemCount
        return itemCount
    }

    override fun getItemViewType(position: Int): Int {
        return if ((!recipes.isEmpty()) && (position < recipes.size)) ITEM else PROGRESS_VIEW
    }

    fun setData(recipes: List<Recipe>) {
        isLoadMoreItems = true
        this.recipes.clear()
        this.recipes.addAll(recipes)
        notifyDataSetChanged()
    }

    fun setMoreData(moreRecipes: List<Recipe>) {
        val insertPosition = recipes.size
        isLoadMoreItems = !moreRecipes.isEmpty()

        if (moreRecipes.isEmpty()) {
            notifyItemRemoved(itemCount - 1)
        } else {
            recipes.addAll(moreRecipes)
            notifyItemRangeInserted(insertPosition, moreRecipes.size)
        }
    }

    inner class RecipeViewHolder(val binding: RecipeListItemBinding) : BaseViewHolder(binding.root), View.OnClickListener {

        override fun onClick(view: View) {
            onItemClickListener.onItemClicked(binding.viewModel!!.getRecipe(), binding.ivRecipeImage)
        }

        override fun bindViewModel() {
            val recipe = recipes.get(adapterPosition)

            if (binding.viewModel == null)
                binding.viewModel = RecipeListItemViewModel(recipe)
            else
                binding.viewModel!!.setRecipe(recipe)

            binding.root.setOnClickListener(this)
            ViewCompat.setTransitionName(binding.ivRecipeImage, binding.root.context
                    .getString(R.string.shared_image_prefix, adapterPosition.toString()))
        }
    }

    interface OnLastItemShownListener {
        fun onLastItemShown()
    }

    interface OnItemClickListener {
        fun onItemClicked(recipe: Recipe, imageView: ImageView)
    }

    fun isEmpty(): Boolean {
        return recipes.isEmpty()
    }

}
