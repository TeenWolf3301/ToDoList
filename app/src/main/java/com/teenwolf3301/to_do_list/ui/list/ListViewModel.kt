package com.teenwolf3301.to_do_list.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.teenwolf3301.to_do_list.data.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val taskDao: TaskDao
) : ViewModel() {

    val list = taskDao.getTasks().asLiveData()
}