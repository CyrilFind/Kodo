package com.cyrilfind.kodo.ui.tasklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrilfind.kodo.data.TasksRepository
import com.cyrilfind.kodo.model.Task
import kotlinx.coroutines.launch

class TaskListViewModel : ViewModel() {
    var showCompleted = true
    var reverseOrder = true
    private val todoRepository = TasksRepository()

    private val _tasksListLiveData = MutableLiveData<List<Task>>()
    val tasksListLiveData: LiveData<List<Task>> = _tasksListLiveData

    fun refreshTasks() {
        viewModelScope.launch {
            val tasks = todoRepository.getTasks(reverseOrder, showCompleted)
            _tasksListLiveData.value = tasks.orEmpty()
        }
    }

    fun add(task: Task) {
        viewModelScope.launch {
            todoRepository.createTask(task)?.let { task ->
                val newList = getMutableList()
                newList.add(task)
                _tasksListLiveData.value = newList
            }
        }
    }

    private fun getMutableList() = _tasksListLiveData.value.orEmpty().toMutableList()

    fun edit(task: Task) {
        viewModelScope.launch {
            todoRepository.updateTask(task)?.let { task ->
                _tasksListLiveData.value = getMutableList().apply {
                    val position = indexOfFirst { task.id == it.id }
                    set(position, task)
                }
            }
        }
    }

    fun delete(task: Task) {
        viewModelScope.launch {
            if (todoRepository.deleteTask(task)) {
                _tasksListLiveData.value = getMutableList().apply {
                    remove(task)
                }
            }
        }
    }

    fun updateChecked(task: Task) {
        viewModelScope.launch {
            //            task.completed = todoRepository.checkTask(task, isChecked) && isChecked
        }
    }
}
