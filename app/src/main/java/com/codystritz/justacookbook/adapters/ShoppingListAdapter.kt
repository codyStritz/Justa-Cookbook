package com.codystritz.justacookbook.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codystritz.justacookbook.model.Ingredient
import com.codystritz.justacookbook.databinding.ShoppingListItemBinding

// Adapter takes as an argument a method that is to be called when a checkbox is checked/unchecked
class ShoppingListAdapter(private val onCheckedChanged: (Ingredient, Boolean) -> Unit) :
    ListAdapter<Ingredient, ShoppingListAdapter.ShoppingListViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListViewHolder {
        return ShoppingListViewHolder(
            ShoppingListItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ShoppingListViewHolder, position: Int) {
        val current = getItem(position)
        // Call the method that was passed as an argument to the adapter
        holder.checkbox.setOnCheckedChangeListener { _, _ ->
            onCheckedChanged(current, holder.checkbox.isChecked)
        }
        holder.bind(current)
    }

    class ShoppingListViewHolder(private var binding: ShoppingListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val checkbox = binding.checkbox
        fun bind(shoppingListItem: Ingredient) {
            binding.apply {
                ingredientName.text = shoppingListItem.ingredientName
                ingredientAmount.text = shoppingListItem.ingredientAmount
                checkbox.isChecked = shoppingListItem.isChecked
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Ingredient>() {
            override fun areItemsTheSame(
                oldItem: Ingredient,
                newItem: Ingredient
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Ingredient,
                newItem: Ingredient
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}