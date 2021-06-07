package com.teenwolf3301.to_do_list.ui.list

import androidx.lifecycle.*
import com.teenwolf3301.to_do_list.data.PreferencesRepository
import com.teenwolf3301.to_do_list.data.SortOrder
import com.teenwolf3301.to_do_list.data.Task
import com.teenwolf3301.to_do_list.data.TaskDao
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

    private val itemEventChannel = Channel<ItemEvent>()
    val itemEvent = itemEventChannel.receiveAsFlow()

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
        itemEventChannel.send(ItemEvent.NavigateToEditItemScreen(task))
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

    fun addNewItemOnClick() = viewModelScope.launch {
        itemEventChannel.send(ItemEvent.NavigateToAddItemScreen)
    }

    sealed class ItemEvent {
        object NavigateToAddItemScreen : ItemEvent()
        data class NavigateToEditItemScreen(val task: Task) : ItemEvent()
    }
}