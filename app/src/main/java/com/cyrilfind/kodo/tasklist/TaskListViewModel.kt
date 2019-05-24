package com.cyrilfind.kodo.tasklist

import androidx.lifecycle.*
import com.cyrilfind.kodo.model.Task
import com.cyrilfind.kodo.network.TasksRepository
import kotlinx.coroutines.launch

class TaskListViewModel(val navigator: TaskNavigator) : ViewModel(), TaskListAdapter.Listener {
    var showCompleted = true
    var reverseOrder = true
    private val tasksList = mutableListOf<Task>()
    private var _shouldScrollPosition = MutableLiveData<Int>()
    val shouldScrollPosition: LiveData<Int>
        get() = _shouldScrollPosition

    private var _isRefreshing = MutableLiveData<Boolean>(false)
    val isRefreshing: LiveData<Boolean>
        get() = _isRefreshing

    private val todoRepository = TasksRepository()
    val recyclerAdapter = TaskListAdapter(tasksList, this)

    override fun onClickItem(position: Int) {
        navigator.goToTaskDetail(tasksList[position])
    }

    override fun onClickDelete(position: Int) {
        viewModelScope.launch {
            val task = tasksList[position]
            if (todoRepository.deleteTask(task)) {
                tasksList.remove(task)
                _shouldScrollPosition.postValue(position)
                recyclerAdapter.notifyItemRemoved(position)
            }
        }
    }

    override fun onClickCheckbox(position: Int, isChecked: Boolean, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            val task = tasksList[position]
            task.completed = todoRepository.checkTask(task, isChecked) && isChecked
            callback(task.completed)
        }
    }

    fun addTaskToList(text: String) {
        viewModelScope.launch {
            todoRepository.createTask(text)?.let { task ->
                val position = 0
                tasksList.add(position, task)
                _shouldScrollPosition.postValue(position)
                recyclerAdapter.notifyItemInserted(position)
            }
        }
    }


    fun refreshTasks() {
        viewModelScope.launch {
            _isRefreshing.postValue(true)
            todoRepository.getTasks(reverseOrder, showCompleted)?.let { tasks ->
                tasksList.clear()
                tasksList.addAll(tasks)
                recyclerAdapter.notifyDataSetChanged()
            }
            _isRefreshing.postValue(false)
        }
    }

    class Factory(private val navigator: TaskNavigator) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return TaskListViewModel(navigator) as T
        }
    }
}
