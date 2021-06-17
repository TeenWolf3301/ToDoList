package com.teenwolf3301.to_do_list.ui.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.teenwolf3301.to_do_list.R
import com.teenwolf3301.to_do_list.data.Priority
import com.teenwolf3301.to_do_list.data.Task
import com.teenwolf3301.to_do_list.data.TaskDao
import com.teenwolf3301.to_do_list.util.ADD_ITEM_RESULT_OK
import com.teenwolf3301.to_do_list.util.APP_ACTIVITY
import com.teenwolf3301.to_do_list.util.DELETE_ITEM_RESULT_OK
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
    private val categoriesDefaultList: Array<String> = APP_ACTIVITY.resources.getStringArray(R.array.category)
    val categoriesUserList = taskDao.getTasksCategories().asLiveData()

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

    fun onDeleteClick() {
        if (task != null) deleteTask(task)
    }

    fun addDefaultList(list: List<String>): List<String> {
        val fullList = list.distinct().toMutableList()
        categoriesDefaultList.forEach {
            if (!fullList.contains(it)) fullList.add(it)
        }
        return fullList.toList()
    }

    private fun addNewTask(task: Task) = viewModelScope.launch {
        taskDao.insertTask(task)
        detailsEventChannel.send(DetailsEvent.NavigateBackWhenClick(ADD_ITEM_RESULT_OK))
    }

    private fun updateTask(task: Task) = viewModelScope.launch {
        taskDao.updateTask(task)
        detailsEventChannel.send(DetailsEvent.NavigateBackWhenClick(UPDATE_ITEM_RESULT_OK))
    }

    private fun deleteTask(task: Task) = viewModelScope.launch {
        taskDao.deleteTask(task)
        detailsEventChannel.send(DetailsEvent.NavigateBackWhenDeleteClick(DELETE_ITEM_RESULT_OK, task))
    }

    private fun showErrorMessage(text: String) = viewModelScope.launch {
        detailsEventChannel.send(DetailsEvent.ErrorWhenSaveClick(text))
    }

    sealed class DetailsEvent {
        data class ErrorWhenSaveClick(val msg: String) : DetailsEvent()
        data class NavigateBackWhenClick(val result: Int) : DetailsEvent()
        data class NavigateBackWhenDeleteClick(val result: Int, val task: Task) : DetailsEvent()
    }
}