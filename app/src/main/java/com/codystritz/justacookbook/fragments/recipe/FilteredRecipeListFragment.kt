package com.codystritz.justacookbook.fragments.recipe

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.codystritz.justacookbook.utils.CookbookApplication
import com.codystritz.justacookbook.R
import com.codystritz.justacookbook.adapters.RecipeListAdapter
import com.codystritz.justacookbook.databinding.FragmentFilteredRecipeListBinding
import com.codystritz.justacookbook.viewmodel.FilteredRecipeListViewModel
import com.codystritz.justacookbook.viewmodel.FilteredRecipeListViewModelFactory
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback


class FilteredRecipeListFragment : Fragment() {

    private var _binding: FragmentFilteredRecipeListBinding? = null
    private val binding get() = _binding!!
    private var interstitialAd: InterstitialAd? = null

    private val navigationArgs: FilteredRecipeListFragmentArgs by navArgs()

    private val viewModel: FilteredRecipeListViewModel by activityViewModels {
        FilteredRecipeListViewModelFactory(
            (activity?.application as CookbookApplication).database.recipeDao(),
            (activity?.application as CookbookApplication).database.ingredientDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFilteredRecipeListBinding.inflate(inflater, container, false)
        // Ads
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(requireContext(), getString(R.string.adUnitId), adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(p0: LoadAdError) {
                interstitialAd = null
            }
            override fun onAdLoaded(p0: InterstitialAd) {
                interstitialAd = p0
            }
        })
        // Do every time
        val adapter = RecipeListAdapter {
            if (interstitialAd != null) {
                interstitialAd?.show(requireActivity())
            }
            val action = FilteredRecipeListFragmentDirections
                .actionFilteredRecipeListFragmentToRecipeDetailFragment(it.id)
            this.findNavController().navigate(action)
        }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.shoppingBtn.setOnClickListener {
            val action = FilteredRecipeListFragmentDirections
                .actionFilteredRecipeListFragmentToShoppingListFragment()
            findNavController().navigate(action)
        }
        binding.clearBtn.setOnClickListener { clearMealPlan() }

        // Meal Plan
        if (navigationArgs.isMealPlan == 1) {
            viewModel.mealPlan.observe(viewLifecycleOwner) { recipes ->
                binding.shoppingBtn.visibility =
                    if (recipes.isNotEmpty()) View.VISIBLE else View.GONE
                binding.clearBtn.visibility = if (recipes.isNotEmpty()) View.VISIBLE else View.GONE
                adapter.submitList(recipes)
            }
        }

        // Category Filter
        else {
            binding.shoppingBtn.visibility = View.GONE
            binding.clearBtn.visibility = View.GONE
            viewModel.getFilteredRecipes(navigationArgs.category)
                .observe(this.viewLifecycleOwner) { recipes ->
                    recipes.let {
                        adapter.submitList(it)
                    }
                }
        }
        return binding.root
    }

    private fun clearMealPlan() {
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.apply {
            setTitle(getString(R.string.clear_meal_plan))
            setMessage(getString(R.string.sure_want_to_clear_meal_plan))
            setPositiveButton(getString(R.string.yes_clear_it)) { _, _ ->
                viewModel.clearMealPlan()
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