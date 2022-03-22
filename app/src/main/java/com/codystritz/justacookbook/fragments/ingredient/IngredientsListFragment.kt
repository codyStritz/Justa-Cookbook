package com.codystritz.justacookbook.fragments.ingredient

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.codystritz.justacookbook.utils.CookbookApplication
import com.codystritz.justacookbook.adapters.IngredientListAdapter
import com.codystritz.justacookbook.model.Ingredient
import com.codystritz.justacookbook.databinding.FragmentIngredientsListBinding
import com.codystritz.justacookbook.viewmodel.IngredientsListViewModel
import com.codystritz.justacookbook.viewmodel.IngredientsListViewModelFactory


class IngredientsListFragment : Fragment() {

    private var _binding: FragmentIngredientsListBinding? = null
    private val binding get() = _binding!!

    private val navigationArgs: IngredientsListFragmentArgs by navArgs()

    private var recipeId: Int? = null

    private val viewModel: IngredientsListViewModel by activityViewModels {
        IngredientsListViewModelFactory(
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
        _binding = FragmentIngredientsListBinding.inflate(inflater, container, false)

        if (navigationArgs.recipeId > 0) {
            recipeId = navigationArgs.recipeId
            val adapter = IngredientListAdapter {
                val action = IngredientsListFragmentDirections
                    .actionIngredientsListFragmentToAddIngredientFragment(
                        recipeId = recipeId!!,
                        ingredientId = it.id,
                        addEdit = "Edit"
                    )
                findNavController().navigate(action)
            }
            binding.ingredientsRecycler.adapter = adapter
            viewModel.getRecipeIngredients(recipeId).observe(viewLifecycleOwner) { ingredients ->
                submitIngredientsList(ingredients, adapter)
            }
        } else {
            viewModel.newestRecipeId.observe(this.viewLifecycleOwner) { newId ->
                recipeId = newId

                val adapter = IngredientListAdapter {
                    val action = IngredientsListFragmentDirections
                        .actionIngredientsListFragmentToAddIngredientFragment(
                            recipeId = recipeId ?: 0,
                            ingredientId = it.id,
                            addEdit = "Edit"
                        )
                    findNavController().navigate(action)
                }
                binding.ingredientsRecycler.adapter = adapter
                viewModel.getRecipeIngredients(recipeId)
                    .observe(this.viewLifecycleOwner) { ingredients ->
                        submitIngredientsList(ingredients, adapter)
                    }
            }
        }
        binding.ingredientsRecycler.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addIngredientBtn.setOnClickListener {
            val action = IngredientsListFragmentDirections
                .actionIngredientsListFragmentToAddIngredientFragment(recipeId ?: 0)
            findNavController().navigate(action)
        }

        binding.nextBtn.setOnClickListener {
            val action = IngredientsListFragmentDirections
                .actionIngredientsListFragmentToDirectionListFragment(
                    recipeId!!,
                    navigationArgs.isEdit
                )
            findNavController().navigate(action)
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val action = IngredientsListFragmentDirections
                    .actionIngredientsListFragmentToAddRecipeFragment(
                        recipeId ?: 0,
                        navigationArgs.isEdit
                    )
                findNavController().navigate(action)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun submitIngredientsList(
        ingredients: List<Ingredient>,
        adapter: IngredientListAdapter
    ) {
        ingredients.let {
            adapter.submitList(ingredients)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                viewModel.deleteRecipe(recipeId)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}