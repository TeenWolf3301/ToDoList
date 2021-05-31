package com.teenwolf3301.to_do_list.ui.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teenwolf3301.to_do_list.R
import com.teenwolf3301.to_do_list.databinding.FragmentListBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.DateFormat

@AndroidEntryPoint
class ListFragment : Fragment(R.layout.fragment_list) {

    private lateinit var listRecyclerView: RecyclerView

    private val viewModel: ListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentListBinding.bind(view)

        val listAdapter = ListAdapter()

        listRecyclerView = binding.rvList
        binding.apply {
            tvDate.text = DateFormat.getDateInstance().format(System.currentTimeMillis())
            tvStats.text = "5 incomplete, 5 completed"

            listRecyclerView.apply {
                adapter = listAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
        }

        viewModel.list.observe(viewLifecycleOwner) {
            listAdapter.submitList(it)
        }
    }
}