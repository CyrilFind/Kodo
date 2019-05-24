package com.cyrilfind.kodo.network

import com.cyrilfind.kodo.model.Task

class TasksRepository {
    private val todoService = TasksApi.tasksService

    suspend fun getTasks(reverse: Boolean = false, completed: Boolean = false): List<Task>? {
        val tasks = mutableListOf<Task>()
        getOpenTasks()?.let { tasks.addAll(it) }
        if (completed) getClosedTasks()?.let { tasks.addAll(it) }
        if (reverse) tasks.sortByDescending { it.id } else tasks.sortBy { it.id }
        return tasks
    }

    private suspend fun getOpenTasks(): List<Task>? {
        val tasksResponse = todoService.getTasks()
        return if (tasksResponse.isSuccessful) tasksResponse.body() else null

    }

    private suspend fun getClosedTasks(): List<Task>? {
        val tasksResponse = todoService.getCompletedTasks()
        return if (tasksResponse.isSuccessful) tasksResponse.body() else null
    }

    suspend fun createTask(text: String): Task? {
        val createTaskResponse = todoService.createTask(Task(text = text))
        return if (createTaskResponse.isSuccessful) createTaskResponse.body() else null
    }

    suspend fun checkTask(task: Task, check: Boolean): Boolean {
        if (task.id == null) return false
        val response = if (check) todoService.checkTask(task.id) else todoService.uncheckTask(task.id)
        return response.isSuccessful
    }

    suspend fun deleteTask(task: Task): Boolean {
        return task.id != null && todoService.deleteTask(task.id).isSuccessful
    }
}
