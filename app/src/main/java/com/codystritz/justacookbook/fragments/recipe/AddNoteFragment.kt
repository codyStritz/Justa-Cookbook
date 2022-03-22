package com.codystritz.justacookbook.fragments.recipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.codystritz.justacookbook.databinding.FragmentAddNoteBinding
import com.codystritz.justacookbook.utils.CookbookApplication
import com.codystritz.justacookbook.utils.Util
import com.codystritz.justacookbook.viewmodel.AddNoteViewModel
import com.codystritz.justacookbook.viewmodel.AddNoteViewModelFactory


class AddNoteFragment : Fragment() {

    private var _binding: FragmentAddNoteBinding? = null
    private val binding get() = _binding!!

    private val navigationArgs: AddNoteFragmentArgs by navArgs()

    private val viewModel: AddNoteViewModel by activityViewModels {
        AddNoteViewModelFactory(
            (activity?.application as CookbookApplication).database.recipeDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.recipeId
        if (savedInstanceState == null) {
            viewModel.retrieveRecipe(id).observe(viewLifecycleOwner) { recipe ->
                binding.noteEditText.setText(recipe.recipeNote)
            }
        }
        binding.saveBtn.setOnClickListener {
            viewModel.updateNote(
                navigationArgs.recipeId,
                binding.noteEditText.text.toString()
            )
            val toRecipeList = AddNoteFragmentDirections.actionAddNoteFragmentToRecipeListFragment()
            val toRecipeDetails = AddNoteFragmentDirections
                .actionAddNoteFragmentToRecipeDetailFragment(navigationArgs.recipeId)
            val action = if (navigationArgs.isEdit) toRecipeDetails else toRecipeList
            findNavController().navigate(action)
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Util.hideKeyboard(requireActivity())
        _binding = null
    }

}