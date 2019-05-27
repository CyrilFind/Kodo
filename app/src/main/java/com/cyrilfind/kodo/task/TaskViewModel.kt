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
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class TaskViewModel(var task: Task) : ViewModel() {
    private var updateJob: Job? = null
    private val tasksRepository = TasksRepository()
    private val simpleDateFormat = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())

    var dateStringLiveData = MutableLiveData(formattedDate)
    var dateLiveData = MutableLiveData(dateTime)

    var completed: Boolean
        get() = task.completed
        set(value) {
            task.completed = value
            updateTask(value)
        }

    var dateTime: Long?
        get() = task.dueDate?.time?.plus(1) ?: System.currentTimeMillis()
        set(value) {
            task.dueDate = value?.let { Date(it.minus(1)) }
            dateStringLiveData.postValue(formattedDate)
            updateTask()
        }

    var formattedDate: String?
        get() = task.dueDate?.time?.let { DateFormat.format(DATE_PATTERN, it).toString() }
        set(value) {
            try {
                task.dueDate = simpleDateFormat.parse(value)
                dateLiveData.postValue(dateTime)
                updateTask()
            } catch (e: ParseException) {
                // don't update Task
            }
        }
    var content
        get() = task.content
        set(value) {
            task.content = value.trim()
            updateTask()
        }

    private fun updateTask() {
        updateJob?.cancel()
        updateJob = viewModelScope.launch {
            delay(DEBOUNCE_TIMEOUT) // debounce
            tasksRepository.updateTask(task)
        }
    }

    suspend fun deleteTask() {
       tasksRepository.deleteTask(task)
    }

    private fun updateTask(checked: Boolean) {
        viewModelScope.launch { tasksRepository.checkTask(task, checked) }
    }

    class Factory(private val task: Task) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return TaskViewModel(task) as T
        }
    }

    companion object {
        private const val DEBOUNCE_TIMEOUT = 500L
        private const val DATE_PATTERN = "dd/MM/yyyy"
    }
}
