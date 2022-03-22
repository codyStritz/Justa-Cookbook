package com.codystritz.justacookbook.fragments.ingredient

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.codystritz.justacookbook.utils.CookbookApplication
import com.codystritz.justacookbook.adapters.ShoppingListAdapter
import com.codystritz.justacookbook.databinding.FragmentShoppingListBinding
import com.codystritz.justacookbook.viewmodel.ShoppingListViewModel
import com.codystritz.justacookbook.viewmodel.ShoppingListViewModelFactory


class ShoppingListFragment : Fragment() {

    private var _binding: FragmentShoppingListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ShoppingListViewModel by activityViewModels {
        ShoppingListViewModelFactory(
            (activity?.application as CookbookApplication).database.ingredientDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentShoppingListBinding.inflate(inflater, container, false)
        val adapter = ShoppingListAdapter { ingredient, isChecked ->
            viewModel.setIsChecked(ingredient.id, isChecked)
        }
        binding.shoppingListRecycler.adapter = adapter
        viewModel.shoppingList.observe(viewLifecycleOwner) { shoppingList ->
            adapter.submitList(shoppingList)
        }
        binding.shoppingListRecycler.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}