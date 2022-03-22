package com.codystritz.justacookbook.fragments.direction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.codystritz.justacookbook.model.Direction
import com.codystritz.justacookbook.databinding.FragmentAddDirectionBinding
import com.codystritz.justacookbook.utils.CookbookApplication
import com.codystritz.justacookbook.utils.Util
import com.codystritz.justacookbook.viewmodel.AddDirectionViewModel
import com.codystritz.justacookbook.viewmodel.AddDirectionViewModelFactory

class AddDirectionFragment : Fragment() {

    private var _binding: FragmentAddDirectionBinding? = null
    private val binding get() = _binding!!

    private val navigationArgs: AddDirectionFragmentArgs by navArgs()

    lateinit var direction: Direction

    private val viewModel: AddDirectionViewModel by activityViewModels {
        AddDirectionViewModelFactory(
            (activity?.application as CookbookApplication).database.directionDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddDirectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // If a directionId is passed as a navArg, direction is being edited
        val directionId = navigationArgs.directionId
        if (directionId > 0) {
            binding.deleteBtn.apply {
                visibility = View.VISIBLE
                setOnClickListener { deleteDirection(directionId) }
            }
            viewModel.getDirection(directionId).observe(viewLifecycleOwner) { selected ->
                if(selected != null) {
                    direction = selected
                    bind(direction)
                }
            }
        } else {
            binding.deleteBtn.visibility = View.GONE
            binding.saveBtn.setOnClickListener { addNewDirection() }
        }
    }

    private fun bind(direction: Direction) {
        binding.directionText.setText(direction.text, TextView.BufferType.SPANNABLE)
        binding.saveBtn.setOnClickListener { updateDirection() }
    }

    private fun deleteDirection(directionId: Int) {
        viewModel.deleteDirection(directionId)
        findNavController().navigateUp()
    }

    private fun updateDirection() {
        if (isDirectionValid()) {
            viewModel.updateDirection(
                directionId = navigationArgs.directionId,
                recipeId = navigationArgs.recipeId,
                text = binding.directionText.text.toString()
            )
            findNavController().navigateUp()
        }
    }

    private fun isDirectionValid(): Boolean {
        return viewModel.isDirectionValid(binding.directionText.text.toString())
    }

    private fun addNewDirection() {
        if (isDirectionValid()) {
            viewModel.addNewDirection(
                recipeId = navigationArgs.recipeId,
                text = binding.directionText.text.toString()
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