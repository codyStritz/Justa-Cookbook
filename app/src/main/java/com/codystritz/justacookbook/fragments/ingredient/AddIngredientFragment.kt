package com.codystritz.justacookbook.fragments.ingredient

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.codystritz.justacookbook.utils.CookbookApplication
import com.codystritz.justacookbook.utils.Util
import com.codystritz.justacookbook.model.Ingredient
import com.codystritz.justacookbook.model.Recipe
import com.codystritz.justacookbook.databinding.FragmentAddIngredientBinding
import com.codystritz.justacookbook.viewmodel.AddIngredientViewModel
import com.codystritz.justacookbook.viewmodel.AddIngredientViewModelFactory

class AddIngredientFragment : Fragment() {

    private var _binding: FragmentAddIngredientBinding? = null
    private val binding get() = _binding!!

    private val navigationArgs: AddIngredientFragmentArgs by navArgs()

    lateinit var ingredient: Ingredient
    lateinit var recipe: Recipe

    private val viewModel: AddIngredientViewModel by activityViewModels {
        AddIngredientViewModelFactory(
            (activity?.application as CookbookApplication).database.recipeDao(),
            (activity?.application as CookbookApplication).database.ingredientDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddIngredientBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ingredientId = navigationArgs.ingredientId
        val recipeId = navigationArgs.recipeId
        if (recipeId > 0) {
            viewModel.retrieveRecipe(navigationArgs.recipeId)
                .observe(viewLifecycleOwner) { recipe ->
                    this.recipe = recipe
                }
        }
        if (ingredientId > 0) {
            binding.deleteBtn.apply {
                visibility = View.VISIBLE
                setOnClickListener { deleteIngredient(ingredientId) }
            }
            viewModel.getIngredient(ingredientId).observe(this.viewLifecycleOwner) { selected ->
                ingredient = selected
                bind(ingredient)
            }
        } else {
            binding.deleteBtn.visibility = View.GONE
            binding.saveBtn.setOnClickListener { addNewIngredient() }
        }
    }

    private fun bind(ingredient: Ingredient) {
        binding.apply {
            ingredientName.setText(ingredient.ingredientName, TextView.BufferType.SPANNABLE)
            ingredientAmount.setText(ingredient.ingredientAmount, TextView.BufferType.SPANNABLE)
            saveBtn.setOnClickListener { updateIngredient() }
        }
    }

    private fun deleteIngredient(ingredientId: Int) {
        viewModel.deleteIngredient(ingredientId)
        findNavController().navigateUp()
    }

    private fun updateIngredient() {
        if (isIngredientValid()) {
            viewModel.updateIngredient(
                navigationArgs.ingredientId,
                binding.ingredientName.text.toString(),
                binding.ingredientAmount.text.toString()
            )
            findNavController().navigateUp()
        }
    }

    private fun isIngredientValid(): Boolean {
        return viewModel.isIngredientValid(binding.ingredientName.text.toString())
    }

    private fun addNewIngredient() {
        if (isIngredientValid()) {
            val amountInput = binding.ingredientAmount.text.toString()
            val amount = if (amountInput.isBlank()) "" else amountInput
            val isOnShoppingList = if (navigationArgs.recipeId > 0) recipe.isInMealPlan else false
            viewModel.addNewIngredient(
                navigationArgs.recipeId,
                binding.ingredientName.text.toString(),
                amount,
                isOnShoppingList
            )
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Util.hideKeyboard(requireActivity())
        _binding = null
    }

}