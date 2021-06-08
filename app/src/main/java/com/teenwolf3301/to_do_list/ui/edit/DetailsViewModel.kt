package com.teenwolf3301.to_do_list.ui.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teenwolf3301.to_do_list.data.Priority
import com.teenwolf3301.to_do_list.data.Task
import com.teenwolf3301.to_do_list.data.TaskDao
import com.teenwolf3301.to_do_list.util.ADD_ITEM_RESULT_OK
import com.teenwolf3301.to_do_list.util.UPDATE_ITEM_RESULT_OK
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val state: SavedStateHandle
) : ViewModel() {

    private val detailsEventChannel = Channel<DetailsEvent>()

    val detailsChannel = detailsEventChannel.receiveAsFlow()
    val task = state.get<Task>("task")

    var taskName = state.get<String>("taskName") ?: task?.name ?: ""
        set(value) {
            field = value
            state.set("taskName", value)
        }

    var taskCategory = state.get<String>("taskCategory") ?: task?.category ?: ""
        set(value) {
            field = value
            state.set("taskCategory", value)
        }

    var taskPriority = state.get<Priority>("taskPriority") ?: task?.priority ?: Priority.LOW
        set(value) {
            field = value
            state.set("taskPriority", value)
        }

    fun onSaveClick() {
        when {
            taskName.isBlank() -> {
                showErrorMessage("Task name is empty!")
                return
            }
            taskCategory.isBlank() -> {
                showErrorMessage("Choose category!")
                return
            }
            task != null -> {
                val updatedTask =
                    task.copy(name = taskName, category = taskCategory, priority = taskPriority)
                updateTask(updatedTask)
            }
            else -> {
                val newTask =
                    Task(name = taskName, category = taskCategory, priority = taskPriority)
                addNewTask(newTask)
            }
        }
    }

    private fun addNewTask(task: Task) = viewModelScope.launch {
        taskDao.insertTask(task)
        detailsEventChannel.send(DetailsEvent.NavigateBackWhenSaveClick(ADD_ITEM_RESULT_OK))
    }

    private fun updateTask(task: Task) = viewModelScope.launch {
        taskDao.updateTask(task)
        detailsEventChannel.send(DetailsEvent.NavigateBackWhenSaveClick(UPDATE_ITEM_RESULT_OK))
    }

    private fun showErrorMessage(text: String) = viewModelScope.launch {
        detailsEventChannel.send(DetailsEvent.ErrorWhenSaveClick(text))
    }

    sealed class DetailsEvent {
        data class ErrorWhenSaveClick(val msg: String) : DetailsEvent()
        data class NavigateBackWhenSaveClick(val result: Int) : DetailsEvent()
    }
}