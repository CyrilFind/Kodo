package com.cyrilfind.kodo.network

import com.cyrilfind.kodo.model.Task

class TasksRepository {
    private val todoService = TasksApiFactory().tasksService

    suspend fun getTasks(): List<Task>? {
        return todoService.getTasks()?.sortedByDescending { it.id }
    }

    suspend fun createTask(text: String): Task? {
        return todoService.createTask(Task(text = text))
    }

    suspend fun deleteTask(task: Task): Boolean {
        val response = task.id?.let {
            todoService.deleteTask(it)
        }
        return response?.isSuccessful ?: false
    }
    suspend fun checkTask(task: Task, check: Boolean): Boolean {
        val response = task.id?.let {
            if (check) todoService.checkTask(it) else todoService.uncheckTask(it)
        }
        return if (response?.isSuccessful == true) check else !check
    }
}
