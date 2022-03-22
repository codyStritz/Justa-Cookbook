package com.codystritz.justacookbook.fragments.direction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.codystritz.justacookbook.adapters.DirectionListAdapter
import com.codystritz.justacookbook.databinding.FragmentDirectionListBinding
import com.codystritz.justacookbook.utils.CookbookApplication
import com.codystritz.justacookbook.viewmodel.DirectionListViewModel
import com.codystritz.justacookbook.viewmodel.DirectionListViewModelFactory


class DirectionListFragment : Fragment() {

    private var _binding: FragmentDirectionListBinding? = null
    private val binding get() = _binding!!

    private val navigationArgs: DirectionListFragmentArgs by navArgs()

    private val viewModel: DirectionListViewModel by activityViewModels {
        DirectionListViewModelFactory(
            (activity?.application as CookbookApplication).database.directionDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDirectionListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // This creates the adapter and passes an onClick method as an argument
        val adapter = DirectionListAdapter {
            val action = DirectionListFragmentDirections
                .actionDirectionListFragmentToAddDirectionFragment(
                    recipeId = navigationArgs.recipeId,
                    directionId = it.id,
                    addEdit = "Edit"
                )
            findNavController().navigate(action)
        }
        binding.directionsRecycler.adapter = adapter
        viewModel.getRecipeDirections(navigationArgs.recipeId)
            .observe(viewLifecycleOwner) { directions ->
                adapter.submitList(directions)
            }
        binding.directionsRecycler.layoutManager = LinearLayoutManager(context)

        binding.addDirectionBtn.setOnClickListener {
            val action = DirectionListFragmentDirections
                .actionDirectionListFragmentToAddDirectionFragment(navigationArgs.recipeId)
            findNavController().navigate(action)
        }

        binding.nextBtn.setOnClickListener {
            val action = DirectionListFragmentDirections
                .actionDirectionListFragmentToAddNoteFragment(
                    recipeId = navigationArgs.recipeId,
                    isEdit = navigationArgs.isEdit
                )
            findNavController().navigate(action)
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val action = DirectionListFragmentDirections
                    .actionDirectionListFragmentToIngredientsListFragment(
                        navigationArgs.recipeId, navigationArgs.isEdit
                    )
                findNavController().navigate(action)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}