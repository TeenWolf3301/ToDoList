package com.teenwolf3301.to_do_list.ui.list

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teenwolf3301.to_do_list.R
import com.teenwolf3301.to_do_list.data.SortOrder
import com.teenwolf3301.to_do_list.data.Task
import com.teenwolf3301.to_do_list.databinding.FragmentListBinding
import com.teenwolf3301.to_do_list.util.onQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint
import java.text.DateFormat

@AndroidEntryPoint
class ListFragment : Fragment(R.layout.fragment_list), ListAdapter.OnItemClickListener {

    private lateinit var listRecyclerView: RecyclerView
    private lateinit var searchView: SearchView

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
/*                addItemDecoration(ListHeader(VIEW_COMPLETED, "Completed"))
                addItemDecoration(ListHeader(VIEW_UNCOMPLETED, "Uncompleted"))*/
                setHasFixedSize(true)
            }
        }

        viewModel.list.observe(viewLifecycleOwner) { list ->
            listAdapter.submitList(list) {
                binding.tvStats.text = resources.getString(
                    R.string.stats,
                    list.count { !it.isCompleted },
                    list.count { it.isCompleted }
                )
            }
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_menu, menu)

        val searchItem = menu.findItem(R.id.list_menu_search)
        searchView = searchItem.actionView as SearchView

        val previousQuery = viewModel.searchQuery.value
        if (!previousQuery.isNullOrBlank()) {
            searchItem.expandActionView()
            searchView.setQuery(previousQuery, false)
        }

        searchView.onQueryTextChanged {
            viewModel.searchQuery.value = it
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.list_menu_sort_by_date -> {
                viewModel.sortOrder.value = SortOrder.BY_DATE
                true
            }
            R.id.list_menu_sort_by_name -> {
                viewModel.sortOrder.value = SortOrder.BY_NAME
                true
            }
            R.id.list_menu_sort_by_priority -> {
                viewModel.sortOrder.value = SortOrder.BY_PRIORITY
                true
            }
            R.id.list_menu_hide_completed -> {
                item.isChecked = !item.isChecked
                viewModel.hideCompleted.value = item.isChecked
                true
            }
            R.id.list_menu_delete_completed -> {
                // TODO
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onItemClick(task: Task) {
        viewModel.onTaskSelected(task)
    }

    override fun onCheckBoxClick(task: Task, isChecked: Boolean) {
        viewModel.onTaskCheckedChanged(task, isChecked)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchView.setOnQueryTextListener(null)
    }
}