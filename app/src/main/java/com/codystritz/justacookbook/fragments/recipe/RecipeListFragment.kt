package com.codystritz.justacookbook.fragments.recipe

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.codystritz.justacookbook.utils.CookbookApplication
import com.codystritz.justacookbook.R
import com.codystritz.justacookbook.adapters.RecipeListAdapter
import com.codystritz.justacookbook.databinding.FragmentRecipeListBinding
import com.codystritz.justacookbook.viewmodel.RecipeListViewModel
import com.codystritz.justacookbook.viewmodel.RecipeListViewModelFactory
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback


class RecipeListFragment : Fragment() {

    private var _binding: FragmentRecipeListBinding? = null
    private val binding get() = _binding!!
    private var interstitialAd: InterstitialAd? = null

    lateinit var adapter: RecipeListAdapter

    private val viewModel: RecipeListViewModel by activityViewModels {
        RecipeListViewModelFactory(
            (activity?.application as CookbookApplication).database.recipeDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeListBinding.inflate(inflater, container, false)
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(requireContext(), getString(R.string.adUnitId), adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(p0: LoadAdError) {
                interstitialAd = null
            }
            override fun onAdLoaded(p0: InterstitialAd) {
                interstitialAd = p0
            }
        })
        adapter = RecipeListAdapter {
            binding.searchView.setQuery(null, false)
            if (interstitialAd != null) {
                interstitialAd?.show(requireActivity())
            }
            val action = RecipeListFragmentDirections
                .actionRecipeListFragmentToRecipeDetailFragment(it.id)
            this.findNavController().navigate(action)
        }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        viewModel.allRecipes.observe(this.viewLifecycleOwner) { recipes ->
            adapter.submitList(recipes)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.floatingActionButton.setOnClickListener {
            val action = RecipeListFragmentDirections.actionRecipeListFragmentToAddRecipeFragment()
            findNavController().navigate(action)
        }
        binding.searchView.setQuery(null, false)
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(p0: String?): Boolean {
                viewModel.getSearchResults(p0).observe(viewLifecycleOwner) { searchResults ->
                    adapter.submitList(searchResults)
                }
                return false
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.recipe_list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.meal_plan_btn -> {
                binding.searchView.setQuery(null, false)
                val action = RecipeListFragmentDirections
                    .actionRecipeListFragmentToFilteredRecipeListFragment(isMealPlan = 1)
                findNavController().navigate(action)
            }
            R.id.search_btn -> {
                search()
            }
            R.id.about_btn -> {
                val action = RecipeListFragmentDirections.actionRecipeListFragmentToAboutFragment()
                findNavController().navigate(action)
            }
            else -> {
                if (item.itemId != R.id.filter_btn) {
                    binding.searchView.setQuery(null, false)
                    goToFilteredList(item.title.toString())
                }
            }
        }
        return true
    }

    private fun search() {
        if (binding.searchView.visibility == View.GONE) {
            binding.searchView.visibility = View.VISIBLE
        } else {
            binding.searchView.visibility = View.GONE
            binding.searchView.setQuery(null, false)
        }
    }

    private fun goToFilteredList(category: String) {
        val action = RecipeListFragmentDirections
            .actionRecipeListFragmentToFilteredRecipeListFragment(category)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}