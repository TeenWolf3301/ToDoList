package com.teenwolf3301.to_do_list.ui.edit

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
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
        }
    }
}