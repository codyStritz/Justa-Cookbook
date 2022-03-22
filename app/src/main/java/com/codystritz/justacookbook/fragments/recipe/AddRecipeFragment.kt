package com.codystritz.justacookbook.fragments.recipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.codystritz.justacookbook.*
import com.codystritz.justacookbook.model.Recipe
import com.codystritz.justacookbook.databinding.FragmentAddRecipeBinding
import com.codystritz.justacookbook.utils.CookbookApplication
import com.codystritz.justacookbook.utils.Util
import com.codystritz.justacookbook.viewmodel.AddRecipeViewModel
import com.codystritz.justacookbook.viewmodel.AddRecipeViewModelFactory


class AddRecipeFragment : Fragment() {

    private var _binding: FragmentAddRecipeBinding? = null
    private val binding get() = _binding!!

    private val navigationArgs: AddRecipeFragmentArgs by navArgs()

    lateinit var recipe: Recipe

    private val viewModel: AddRecipeViewModel by activityViewModels {
        AddRecipeViewModelFactory(
            (activity?.application as CookbookApplication).database.recipeDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(binding.recipeName.text.toString())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.recipeId
        binding.cancelBtn.setOnClickListener { findNavController().navigateUp() }
        if (id > 0) {
            viewModel.retrieveRecipe(id).observe(viewLifecycleOwner) { selectedRecipe ->
                recipe = selectedRecipe
                bind(recipe)
            }
        } else {
            binding.nextBtn.setOnClickListener {
                addNewItem()
            }
            binding.categorySpinner.setSelection(0)
        }
    }

    private fun bind(recipe: Recipe) {
        val categoryPosition = requireContext().resources.getStringArray(R.array.categories)
            .indexOf(recipe.recipeCategory)
        binding.apply {
            recipeName.setText(recipe.recipeName, TextView.BufferType.SPANNABLE)
            categorySpinner.setSelection(categoryPosition)
            recipeServings.setText(recipe.servings, TextView.BufferType.SPANNABLE)
            nextBtn.setOnClickListener { updateRecipe() }
        }
    }

    private fun addNewItem() {
        if(isEntryValid()) {
            viewModel.addNewRecipe(
                binding.recipeName.text.toString(),
                binding.categorySpinner.selectedItem.toString(),
                binding.recipeServings.text?.toString()?:""
            )
            val action = AddRecipeFragmentDirections
                .actionAddRecipeFragmentToIngredientsListFragment()
            findNavController().navigate(action)
        } else {
            binding.recipeName.error = "Recipe name required"

        }

    }

    private fun updateRecipe() {
        if(isEntryValid()) {
            viewModel.updateRecipe(
                navigationArgs.recipeId,
                binding.recipeName.text.toString(),
                binding.categorySpinner.selectedItem.toString(),
                binding.recipeServings.text?.toString()?:""
            )
            val action = AddRecipeFragmentDirections
                .actionAddRecipeFragmentToIngredientsListFragment(
                    navigationArgs.recipeId,
                    navigationArgs.isEdit
                )
            findNavController().navigate(action)
        } else {
            binding.recipeName.error = "Recipe name required"
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        Util.hideKeyboard(requireActivity())
        _binding = null
    }

}