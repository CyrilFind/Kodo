package com.cyrilfind.kodo.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cyrilfind.kodo.model.Task


class TaskViewModel(var task: Task) : ViewModel() {
    class Factory(private val task: Task) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return TaskViewModel(task) as T
        }
    }
}
