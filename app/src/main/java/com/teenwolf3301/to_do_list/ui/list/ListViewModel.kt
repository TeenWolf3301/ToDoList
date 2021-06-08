package com.teenwolf3301.to_do_list.ui.list

import androidx.lifecycle.*
import com.teenwolf3301.to_do_list.data.PreferencesRepository
import com.teenwolf3301.to_do_list.data.SortOrder
import com.teenwolf3301.to_do_list.data.Task
import com.teenwolf3301.to_do_list.data.TaskDao
import com.teenwolf3301.to_do_list.util.ADD_ITEM_RESULT_OK
import com.teenwolf3301.to_do_list.util.UPDATE_ITEM_RESULT_OK
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val preferencesRepository: PreferencesRepository,
    state: SavedStateHandle
) : ViewModel() {

    val searchQuery = state.getLiveData("searchQuery", "")

    val preferencesFlow = preferencesRepository.preferencesFlow

    private val listEventChannel = Channel<ListEvent>()
    val listEvent = listEventChannel.receiveAsFlow()

    private val taskFlow = combine(
        searchQuery.asFlow(),
        preferencesFlow
    ) { query, filterPreferences ->
        Pair(query, filterPreferences)
    }.flatMapLatest { (query, filterPreferences) ->
        taskDao.getTasks(query, filterPreferences.sortOrder, filterPreferences.hideCompleted)
    }

    val list = taskFlow.asLiveData()

    fun onTaskSelected(task: Task) = viewModelScope.launch {
        listEventChannel.send(ListEvent.NavigateToEditItemScreen(task))
    }

    fun onTaskCheckedChanged(task: Task, isChecked: Boolean) = viewModelScope.launch {
        taskDao.updateTask(task.copy(isCompleted = isChecked))
    }

    fun onSortOrderChange(sortOrder: SortOrder) = viewModelScope.launch {
        preferencesRepository.updateSortOrder(sortOrder)
    }

    fun onHideCompletedChange(hideCompleted: Boolean) = viewModelScope.launch {
        preferencesRepository.updateHideCompleted(hideCompleted)
    }

    fun onAddNewItemClick() = viewModelScope.launch {
        listEventChannel.send(ListEvent.NavigateToAddItemScreen)
    }

    fun onDetailsResult(result: Int) {
        when (result) {
            ADD_ITEM_RESULT_OK -> showConfirmMessage("Task is added!")
            UPDATE_ITEM_RESULT_OK -> showConfirmMessage("Task is updated!")
        }
    }

    private fun showConfirmMessage(text: String) = viewModelScope.launch {
        listEventChannel.send(ListEvent.ShowConfirmMessage(text))
    }

    sealed class ListEvent {
        object NavigateToAddItemScreen : ListEvent()
        data class NavigateToEditItemScreen(val task: Task) : ListEvent()
        data class ShowConfirmMessage(val msg: String) : ListEvent()
    }
}