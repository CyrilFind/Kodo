package com.cyrilfind.kodo.tasklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrilfind.kodo.model.Task
import com.cyrilfind.kodo.network.TasksRepository
import kotlinx.coroutines.launch

class TaskListViewModel : ViewModel() {
    var reverseOrder = false
    private var _tasksListLiveData = MutableLiveData<List<Task>>()
    private val tasksList = mutableListOf<Task>()
    val tasksListLiveData: LiveData<List<Task>>
        get() = _tasksListLiveData

    private var _isRefreshing = MutableLiveData<Boolean>(false)
    val isRefreshing: LiveData<Boolean>
        get() = _isRefreshing

    private val todoRepository = TasksRepository()
    val recyclerAdapter = TaskListAdapter(tasksList, this::onClickDelete, this::onClickCheckbox)

    private fun onClickDelete(position: Int) {
        viewModelScope.launch {
            val task = tasksListLiveData.value!![position]
            if (todoRepository.deleteTask(task)) {
                tasksList.remove(task)
                notifyLiveData()
                recyclerAdapter.notifyItemRemoved(position)
            }
        }
    }

    private fun onClickCheckbox(position: Int, checked: Boolean, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            val task = tasksList[position]
            task.checked = todoRepository.checkTask(task, checked)
            callback(task.checked)
        }
    }

    fun addTaskToList(text: String) {
        viewModelScope.launch {
            todoRepository.createTask(text)?.let { task ->
                tasksList.add(0, task)
                notifyLiveData()
                recyclerAdapter.notifyItemInserted(0)
            }
        }
    }


    fun refreshTasks() {
        viewModelScope.launch {
            _isRefreshing.postValue(true)
            todoRepository.getTasks(reverseOrder)?.let { tasks ->
                tasksList.clear()
                tasksList.addAll(tasks)
                notifyLiveData()
                recyclerAdapter.notifyDataSetChanged()
            }
            _isRefreshing.postValue(false)
        }
    }

    private fun notifyLiveData() {
        _tasksListLiveData.postValue(tasksList)
    }
}
