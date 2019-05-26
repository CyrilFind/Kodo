package com.cyrilfind.kodo.task

import android.text.format.DateFormat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cyrilfind.kodo.model.Task










class TaskViewModel(var task: Task) : ViewModel() {
    val formattedDate = MutableLiveData<String>("No Date")

    init {
        task.due?.date?.let {
            formattedDate.postValue( DateFormat.format("dd MMMM yyyy", it).toString())
        }
    }

    class Factory(private val task: Task) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return TaskViewModel(task) as T
        }
    }
}
