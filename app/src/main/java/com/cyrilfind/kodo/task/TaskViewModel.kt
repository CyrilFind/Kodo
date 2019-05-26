package com.cyrilfind.kodo.task

import android.text.format.DateFormat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.cyrilfind.kodo.model.Task
import com.cyrilfind.kodo.network.TasksRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class TaskViewModel(var task: Task) : ViewModel() {
    val formattedDate = MutableLiveData<String>("No Date")
    private val tasksRepository = TasksRepository()
    private var updateJob: Job? = null

    init {
        task.due?.date?.let {
            formattedDate.postValue( DateFormat.format("dd MMMM yyyy", it).toString())
        }
    }

    var content = task.content
        set(value) {
            field = value.trim()
            task.content = field
            updateTask()
        }

    private fun updateTask() {
        updateJob?.cancel()
        updateJob = viewModelScope.launch {
            delay(500) // debounce
            tasksRepository.updateTask(task)
        }
    }

    class Factory(private val task: Task) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return TaskViewModel(task) as T
        }
    }
}
