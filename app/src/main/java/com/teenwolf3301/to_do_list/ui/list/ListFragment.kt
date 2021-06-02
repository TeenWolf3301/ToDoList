package com.teenwolf3301.to_do_list.ui.list

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teenwolf3301.to_do_list.R
import com.teenwolf3301.to_do_list.data.Task
import com.teenwolf3301.to_do_list.databinding.FragmentListBinding
import com.teenwolf3301.to_do_list.util.removeItemDecorations
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.DateFormat

@AndroidEntryPoint
class ListFragment : Fragment(R.layout.fragment_list), ListAdapter.OnItemClickListener {

    private lateinit var listRecyclerView: RecyclerView

    private val viewModel: ListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentListBinding.bind(view)
        val listAdapter = ListAdapter(this)

        listRecyclerView = binding.rvList
        binding.apply {
            tvDate.text = DateFormat.getDateInstance().format(System.currentTimeMillis())

            listRecyclerView.apply {
                adapter = listAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
        }

        viewModel.list.observe(viewLifecycleOwner) { list ->
            listAdapter.submitList(list) {
                lifecycleScope.launch {
                    listRecyclerView.scrollToPosition(0)
                    listRecyclerView.removeItemDecorations()
                    addDecorationCompleted(listAdapter.currentList)
                    addDecorationUncompleted(listAdapter.currentList)
                    binding.tvStats.text =
                        "${list.size - viewModel.completedTasks()} uncomplete, ${viewModel.completedTasks()} completed"
                }
            }
        }

        setHasOptionsMenu(true)
    }

    private fun addDecorationCompleted(list: List<Task>) {
        val firstCompleted = list.indexOf(list.firstOrNull { it.isCompleted })
        listRecyclerView.addItemDecoration(ListHeader(firstCompleted, "Completed"))
    }

    private fun addDecorationUncompleted(list: List<Task>) {
        val firstUncompleted = list.indexOf(list.firstOrNull { !it.isCompleted })
        listRecyclerView.addItemDecoration(ListHeader(firstUncompleted, "Uncompleted"))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_menu, menu)

        val searchItem = menu.findItem(R.id.list_menu_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = true

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.searchQuery.value = newText.orEmpty()
                return true
            }
        })
    }

    override fun onItemClick(task: Task) {
        viewModel.onTaskSelected(task)
    }

    override fun onCheckBoxClick(task: Task, isChecked: Boolean) {
        viewModel.onTaskCheckedChanged(task, isChecked)
    }
}