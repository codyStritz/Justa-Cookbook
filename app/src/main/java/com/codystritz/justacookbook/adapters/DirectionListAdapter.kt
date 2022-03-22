package com.codystritz.justacookbook.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codystritz.justacookbook.model.Direction
import com.codystritz.justacookbook.databinding.DirectionListItemBinding

// Adapter takes as an argument a method that is to be called when an item is clicked
class DirectionListAdapter(private val onItemClicked: (Direction) -> Unit) :
    ListAdapter<Direction, DirectionListAdapter.DirectionViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DirectionViewHolder {
        return DirectionViewHolder(
            DirectionListItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DirectionViewHolder, position: Int) {
        val current = getItem(position)
        // Call the the method that was passed as an argument to the adapter
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        holder.bind(current)
    }

    class DirectionViewHolder(private var binding: DirectionListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(direction: Direction) {
            binding.apply {
                directionTxt.text = direction.text
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Direction>() {
            override fun areItemsTheSame(oldItem: Direction, newItem: Direction): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Direction, newItem: Direction): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}