package com.codystritz.justacookbook.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codystritz.justacookbook.model.Ingredient
import com.codystritz.justacookbook.databinding.IngredientListItemBinding

// Adapter takes as an argument a method that is to be called when an item is clicked
class IngredientListAdapter(private val onItemClicked: (Ingredient) -> Unit) :
    ListAdapter<Ingredient, IngredientListAdapter.IngredientViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        return IngredientViewHolder(
            IngredientListItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val current = getItem(position)
        // Call the method that was passed as an argument to the adapter
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        holder.bind(current)
    }

    class IngredientViewHolder(private var binding: IngredientListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(ingredient: Ingredient) {
            binding.apply {
                ingredientName.text = ingredient.ingredientName
                ingredientAmount.text = ingredient.ingredientAmount
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Ingredient>() {
            override fun areItemsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
                return oldItem.ingredientName == newItem.ingredientName
            }
        }
    }
}