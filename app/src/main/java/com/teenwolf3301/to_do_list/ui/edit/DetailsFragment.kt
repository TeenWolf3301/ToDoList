package com.teenwolf3301.to_do_list.ui.edit

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.teenwolf3301.to_do_list.R
import com.teenwolf3301.to_do_list.data.Priority
import com.teenwolf3301.to_do_list.databinding.FragmentDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {

    private val viewModel: DetailsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentDetailsBinding.bind(view)
        val categories = resources.getStringArray(R.array.category)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, categories)

        binding.apply {
            etTaskName.setText(viewModel.taskName)
            when (viewModel.taskPriority) {
                Priority.HIGH -> rgPriority.check(R.id.rb_high)
                Priority.MEDIUM -> rgPriority.check(R.id.rb_medium)
                Priority.LOW -> rgPriority.check(R.id.rb_low)
            }
            rgPriority.jumpDrawablesToCurrentState()
            tvDateCreated.isVisible = viewModel.task != null
            tvDateCreated.text = "Created: ${viewModel.task?.formattedDate}"
            dropdownText.setAdapter(arrayAdapter)
            if (categories.contains(viewModel.taskCategory)) {
                dropdownText.setText(viewModel.taskCategory, false)
            }

            etTaskName.addTextChangedListener {
                viewModel.taskName = it.toString()
            }

            if (dropdownText.isSelected) {
                viewModel.taskCategory = dropdownText.text.toString()
            }

            when (rgPriority.checkedRadioButtonId) {
                R.id.rb_high -> viewModel.taskPriority = Priority.HIGH
                R.id.rb_medium -> viewModel.taskPriority = Priority.MEDIUM
                R.id.rb_low -> viewModel.taskPriority = Priority.LOW
            }

            fabApply.setOnClickListener {
                viewModel.onSaveClick()
            }
        }
    }
}