package com.cyrilfind.kodo.ui.taskform

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrilfind.kodo.data.TasksRepository
import com.cyrilfind.kodo.model.Task
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TaskFormViewModel(var task: Task) : ViewModel() {
    private var updateJob: Job? = null
    private val tasksRepository = TasksRepository()

    var title
        get() = task.title
        set(value) {
            task.title = value?.trim()
            updateTask()
        }

    var description
        get() = task.description
        set(value) {
            task.description = value?.trim()
            updateTask()
        }

    fun updateTask() {
        updateJob?.cancel()
        updateJob = viewModelScope.launch {
            delay(DEBOUNCE_TIMEOUT)
            tasksRepository.updateTask(task)
        }
    }

    suspend fun deleteTask() {
        tasksRepository.deleteTask(task)
    }

    companion object {
        private const val DEBOUNCE_TIMEOUT = 500L
    }
}
