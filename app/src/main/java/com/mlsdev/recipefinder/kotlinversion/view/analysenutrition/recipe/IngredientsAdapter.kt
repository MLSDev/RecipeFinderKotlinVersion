package com.mlsdev.recipefinder.kotlinversion.view.analysenutrition.recipe

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ObservableField
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mlsdev.recipefinder.kotlinversion.R
import com.mlsdev.recipefinder.kotlinversion.databinding.AddIngredientButtonBinding
import com.mlsdev.recipefinder.kotlinversion.databinding.IngredientListItemBinding
import com.mlsdev.recipefinder.kotlinversion.view.analysenutrition.adapter.BaseViewHolder
import com.mlsdev.recipefinder.kotlinversion.view.viewmodel.BaseViewModel

class IngredientsAdapter(private var onAddIngredientClickListener: OnAddIngredientClickListener)
    : RecyclerView.Adapter<BaseViewHolder>() {
    private val ingredientList = ArrayList<String>()
    private val INGREDIENT_VIEW_TYPE = 0
    private val ADD_INGREDIENT_VIEW_TYPE = 1

    override fun getItemViewType(position: Int): Int {
        return if (position < ingredientList.size) INGREDIENT_VIEW_TYPE else ADD_INGREDIENT_VIEW_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        if (viewType == INGREDIENT_VIEW_TYPE) {
            val binding: IngredientListItemBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.ingredient_list_item,
                    parent,
                    false
            )
            return ViewHolder(binding, this)
        } else {
            val binding: AddIngredientButtonBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.add_ingredient_button,
                    parent,
                    false
            )
            return AddIngredientButtonHolder(binding, onAddIngredientClickListener)
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bindViewModel()
    }

    override fun getItemCount(): Int {
        return ingredientList.size + ADD_INGREDIENT_VIEW_TYPE
    }

    private fun addItem(text: String) {
        ingredientList.add(text)
        notifyItemInserted(ingredientList.size - 1)
    }

    fun removeItem(position: Int) {
        ingredientList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, ingredientList.size - 1)
    }

    fun addItem(data: Intent) {
        if (data.hasExtra(AddIngredientDialogFragment.INGREDIENT_TITLE_KEY))
            addItem(data.getStringExtra(AddIngredientDialogFragment.INGREDIENT_TITLE_KEY))
    }

    fun getIngredientList(): List<String> {
        return ingredientList
    }

    class ViewHolder(val binding: IngredientListItemBinding, private val ingredientsAdapter: IngredientsAdapter) : BaseViewHolder(binding.root) {

        override fun bindViewModel() {
            if (binding.viewModel == null)
                binding.viewModel = IngredientViewModel(binding, ingredientsAdapter)

            binding.viewModel!!.setIngredient(ingredientsAdapter.ingredientList[adapterPosition], adapterPosition + 1)
        }
    }

    class AddIngredientButtonHolder(
            val binding: AddIngredientButtonBinding,
            private val onAddIngredientClickListener: OnAddIngredientClickListener) : BaseViewHolder(binding.root) {

        override fun bindViewModel() {
            if (binding.viewModel == null)
                binding.viewModel = AddIngredientViewModel(binding.root.context, onAddIngredientClickListener)
        }
    }

    class IngredientViewModel(val binding: IngredientListItemBinding, private val ingredientsAdapter: IngredientsAdapter) : BaseViewModel(binding.root.context) {
        private var position = 0
        val text: ObservableField<String> = ObservableField()
        val number: ObservableField<String> = ObservableField()

        fun onCancelButtonClick(button: View) {
            ingredientsAdapter.removeItem(position - 1)
        }

        fun setIngredient(text: String, position: Int) {
            this.text.set(text)
            this.position = position
            number.set(context.getString(R.string.ingredient_order_number, position))
        }
    }

    class AddIngredientViewModel(context: Context, private var onAddIngredientClickListener: OnAddIngredientClickListener?) : BaseViewModel(context) {

        fun onAddIngredientButtonClick(view: View) {
            if (onAddIngredientClickListener != null)
                onAddIngredientClickListener!!.onAddIngredientButtonClick()
        }
    }

    fun setData(ingredientList: List<String>) {
        this.ingredientList.clear()
        this.ingredientList.addAll(ingredientList)
        notifyDataSetChanged()
    }
}
