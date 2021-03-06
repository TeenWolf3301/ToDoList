package com.teenwolf3301.to_do_list.ui.list

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.teenwolf3301.to_do_list.R
import com.teenwolf3301.to_do_list.data.PreferencesRepository.SortOrder
import com.teenwolf3301.to_do_list.data.Task
import com.teenwolf3301.to_do_list.databinding.FragmentListBinding
import com.teenwolf3301.to_do_list.ui.list.ListViewModel.ListEvent.*
import com.teenwolf3301.to_do_list.util.VIEW_COMPLETED
import com.teenwolf3301.to_do_list.util.VIEW_UNCOMPLETED
import com.teenwolf3301.to_do_list.util.onQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
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
                addItemDecoration(ListHeader(VIEW_COMPLETED, "Completed"))
                addItemDecoration(ListHeader(VIEW_UNCOMPLETED, "Uncompleted"))
                setHasFixedSize(true)
            }

            fabAddItem.setOnClickListener {
                viewModel.onAddNewItemClick()
            }
        }

        setFragmentResultListener("details_request") { _, bundle ->
            val result = bundle.getInt("details_result")
            if (bundle.containsKey("deleted_task")) {
                val deletedTask = bundle.get("deleted_task") as Task
                viewModel.onDetailsResult(result, deletedTask)
            } else viewModel.onDetailsResult(result)
        }

        viewModel.list.observe(viewLifecycleOwner) { list ->
            listAdapter.submitList(list) {
                viewModel.onEmptyDatabase(list)
                listAdapter.notifyDataSetChanged()
                listRecyclerView.scrollToPosition(0)
                binding.tvStats.text = when {
                    list.count { !it.isCompleted } == 0 -> {
                        resources.getString(R.string.statsCompleted, list.count { it.isCompleted })
                    }
                    list.count { it.isCompleted } == 0 -> {
                        resources.getString(R.string.statsUncompleted, list.count { !it.isCompleted })
                    }
                    else -> {
                        resources.getString(
                            R.string.stats,
                            resources.getString(R.string.statsUncompleted, list.count { !it.isCompleted }),
                            resources.getString(R.string.statsCompleted, list.count { it.isCompleted })
                        )
                    }
                }
            }
        }

        viewModel.emptyDatabase.observe(viewLifecycleOwner) { isEmpty ->
            binding.apply {
                if (isEmpty) {
                    tvStats.visibility = View.GONE
                    tvEmptyDb.visibility = View.VISIBLE
                    ivEmptyDb.visibility = View.VISIBLE
                } else {
                    tvStats.visibility = View.VISIBLE
                    tvEmptyDb.visibility = View.GONE
                    ivEmptyDb.visibility = View.GONE
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.listEvent.collect { event ->
                when (event) {
                    is NavigateToAddItemScreen -> {
                        val action = ListFragmentDirections.actionListFragmentToEditFragment(
                            null,
                            "New Task"
                        )
                        findNavController().navigate(action)
                    }
                    is NavigateToEditItemScreen -> {
                        val action =
                            ListFragmentDirections.actionListFragmentToEditFragment(
                                event.task,
                                "Edit Task"
                            )
                        findNavController().navigate(action)
                    }
                    is ShowConfirmMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_SHORT).show()
                    }
                    is ShowConfirmDeleteMessage -> {
                        Snackbar.make(requireView(), "Task deleted!", Snackbar.LENGTH_SHORT)
                            .setAction("UNDO") {
                                viewModel.onUndoDeleteClick(event.task)
                            }.show()
                    }
                }
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

        viewLifecycleOwner.lifecycleScope.launch {
            menu.findItem(R.id.list_menu_hide_completed).isChecked =
                viewModel.preferencesFlow.first().hideCompleted
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.list_menu_sort_by_date -> {
            viewModel.onSortOrderChange(SortOrder.BY_DATE)
            true
        }
        R.id.list_menu_sort_by_name -> {
            viewModel.onSortOrderChange(SortOrder.BY_NAME)
            true
        }
        R.id.list_menu_sort_by_priority -> {
            viewModel.onSortOrderChange(SortOrder.BY_PRIORITY)
            true
        }
        R.id.list_menu_hide_completed -> {
            item.isChecked = !item.isChecked
            viewModel.onHideCompletedChange(item.isChecked)
            true
        }
        else -> super.onOptionsItemSelected(item)
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