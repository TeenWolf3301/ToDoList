package com.teenwolf3301.to_do_list.ui.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.teenwolf3301.to_do_list.data.Priority
import com.teenwolf3301.to_do_list.data.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val state: SavedStateHandle
) : ViewModel() {

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

    var taskPriority = state.get<String>("taskName") ?: task?.priority ?: Priority.LOW
        set(value) {
            field = value
            state.set("taskName", value)
        }
}