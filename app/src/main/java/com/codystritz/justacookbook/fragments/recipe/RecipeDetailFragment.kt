package com.codystritz.justacookbook.fragments.recipe

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.codystritz.justacookbook.utils.CookbookApplication
import com.codystritz.justacookbook.R
import com.codystritz.justacookbook.adapters.DirectionListAdapter
import com.codystritz.justacookbook.adapters.IngredientListAdapter
import com.codystritz.justacookbook.model.Recipe
import com.codystritz.justacookbook.databinding.FragmentRecipeDetailBinding
import com.codystritz.justacookbook.viewmodel.RecipeDetailViewModel
import com.codystritz.justacookbook.viewmodel.RecipeDetailViewModelFactory


class RecipeDetailFragment : Fragment() {

    private var _binding: FragmentRecipeDetailBinding? = null
    private val binding get() = _binding!!

    private val navigationArgs: RecipeDetailFragmentArgs by navArgs()

    lateinit var recipe: Recipe

    private val viewModel: RecipeDetailViewModel by activityViewModels {
        RecipeDetailViewModelFactory(
            (activity?.application as CookbookApplication).database.recipeDao(),
            (activity?.application as CookbookApplication).database.ingredientDao(),
            (activity?.application as CookbookApplication).database.directionDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)
        val id = navigationArgs.recipeId
        viewModel.retrieveRecipe(id).observe(viewLifecycleOwner) { selectedRecipe ->
            if(selectedRecipe != null) {
                recipe = selectedRecipe
                bind(recipe)
            }
        }

        val ingredientAdapter = IngredientListAdapter {}
        binding.ingredientsRecycler.adapter = ingredientAdapter
        viewModel.getRecipeIngredients(id).observe(viewLifecycleOwner) { ingredients ->
            ingredientAdapter.submitList(ingredients)
        }
        binding.ingredientsRecycler.layoutManager = LinearLayoutManager(context)

        val directionAdapter = DirectionListAdapter {}
        binding.directionsRecycler.adapter = directionAdapter
        viewModel.getRecipeDirections(id).observe(viewLifecycleOwner) { directions ->
            directionAdapter.submitList(directions)
        }
        binding.directionsRecycler.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    private fun bind(recipe: Recipe) {
        binding.apply {
            recipeName.text = recipe.recipeName
            category.text = recipe.recipeCategory
            servingsNumber.text = recipe.servings
            noteText.text = recipe.recipeNote
            deleteBtn.setOnClickListener { deleteRecipe() }
            editBtn.setOnClickListener {
                val action = RecipeDetailFragmentDirections
                    .actionRecipeDetailFragmentToAddRecipeFragment(navigationArgs.recipeId, true)
                findNavController().navigate(action)
            }
            addNoteBtn.setOnClickListener {
                val action = RecipeDetailFragmentDirections
                    .actionRecipeDetailFragmentToAddNoteFragment(
                        recipeId = recipe.id,
                        isEdit = true
                    )
                findNavController().navigate(action)
            }
        }
        when (recipe.isInMealPlan) {
            true -> {
                binding.mealPlanBtn.text = getString(R.string.remove_from_meal_plan)
                binding.mealPlanBtn.icon = AppCompatResources
                    .getDrawable(requireContext(), R.drawable.ic_baseline_playlist_add_check_24)
                binding.mealPlanBtn.setOnClickListener {
                    Toast.makeText(
                        context,
                        getString(R.string.removed_from_meal_plan),
                        Toast.LENGTH_SHORT
                    ).show()
                    viewModel.toggleInMealPlan(recipe)
                    viewModel.setRecipeOnShoppingList(recipe.id, false)
                    viewModel.uncheckRecipeIngredients(recipe.id)
                }
            }
            false -> {
                binding.mealPlanBtn.text = getString(R.string.add_to_meal_plan)
                binding.mealPlanBtn.icon = AppCompatResources
                    .getDrawable(requireContext(), R.drawable.ic_baseline_playlist_add_24)
                binding.mealPlanBtn.setOnClickListener {
                    Toast.makeText(
                        context,
                        getString(R.string.added_to_meal_plan),
                        Toast.LENGTH_SHORT
                    ).show()
                    viewModel.toggleInMealPlan(recipe)
                    viewModel.setRecipeOnShoppingList(recipe.id, true)
                }
            }
        }
        when (recipe.recipeNote.isBlank()) {
            true -> {
                binding.noteLabel.visibility = View.GONE
                binding.noteText.visibility = View.GONE
                binding.addNoteBtn.visibility = View.VISIBLE
                binding.ingredientsCard.updateLayoutParams<ConstraintLayout.LayoutParams> {
                    topToBottom = binding.addNoteBtn.id
                    topMargin = 8
                }
            }
            false -> {
                binding.noteLabel.visibility = View.VISIBLE
                binding.noteText.visibility = View.VISIBLE
                binding.addNoteBtn.visibility = View.GONE
            }
        }
    }

    private fun deleteRecipe() {
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.apply {
            setTitle(getString(R.string.delete_recipe))
            setMessage(getString(R.string.sure_want_delete_recipe))
            setPositiveButton(getString(R.string.yes_delete)) { _, _ ->
                viewModel.deleteRecipe(recipe.id)
                findNavController().navigateUp()
            }
            setNeutralButton(getString(R.string.cancel)) { _, _ -> }
        }.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}