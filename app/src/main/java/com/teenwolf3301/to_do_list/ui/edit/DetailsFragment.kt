package com.teenwolf3301.to_do_list.ui.edit

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.teenwolf3301.to_do_list.R
import com.teenwolf3301.to_do_list.data.Priority
import com.teenwolf3301.to_do_list.databinding.FragmentDetailsBinding
import com.teenwolf3301.to_do_list.ui.edit.DetailsViewModel.DetailsEvent.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {

    private val viewModel: DetailsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentDetailsBinding.bind(view)

        binding.apply {
            etTaskName.setText(viewModel.taskName)
            when (viewModel.taskPriority) {
                Priority.HIGH -> rgPriority.check(R.id.rb_high)
                Priority.MEDIUM -> rgPriority.check(R.id.rb_medium)
                Priority.LOW -> rgPriority.check(R.id.rb_low)
            }
            rgPriority.jumpDrawablesToCurrentState()
            tvDateCreated.isVisible = viewModel.task != null
            tvDateCreated.text =
                resources.getString(R.string.date_created, viewModel.task?.formattedDate)

            etTaskName.addTextChangedListener {
                viewModel.taskName = it.toString()
            }

            dropdownText.addTextChangedListener {
                viewModel.taskCategory = dropdownText.text.toString()
            }

            rgPriority.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.rb_high -> viewModel.taskPriority = Priority.HIGH
                    R.id.rb_medium -> viewModel.taskPriority = Priority.MEDIUM
                    R.id.rb_low -> viewModel.taskPriority = Priority.LOW
                }
            }

            fabApply.setOnClickListener {
                viewModel.onSaveClick()
            }
        }

        viewModel.categoriesUserList.observe(viewLifecycleOwner) {
            val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, viewModel.addDefaultList(it))
            binding.dropdownText.apply {
                setAdapter(arrayAdapter)
                setText(viewModel.taskCategory, false)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.detailsChannel.collect { event ->
                when (event) {
                    is ErrorWhenSaveClick -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_SHORT).show()
                    }
                    is NavigateBackWhenClick -> {
                        binding.etTaskName.clearFocus()
                        setFragmentResult(
                            "details_request",
                            bundleOf("details_result" to event.result)
                        )
                        findNavController().popBackStack()
                    }
                    is NavigateBackWhenDeleteClick -> {
                        binding.etTaskName.clearFocus()
                        setFragmentResult(
                            "details_request",
                            bundleOf("details_result" to event.result, "deleted_task" to event.task)
                        )
                        findNavController().popBackStack()
                    }
                }
            }
        }

        if (viewModel.task != null) setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.details_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.details_menu_delete -> {
            viewModel.onDeleteClick()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}