package com.cyrilfind.kodo.network

import com.cyrilfind.kodo.model.Task

class TasksRepository {
    private val todoService = TasksApi.tasksService

    suspend fun getTasks(reverse: Boolean = false, completed: Boolean = false): List<Task>? {
        val tasks = mutableListOf<Task>()
        val activeTasks = todoService.getTasks() ?: emptyList()
        val completedTasks = if (completed) todoService.getCompletedTasks()?.map { it.toTask() } ?: emptyList() else emptyList()
        tasks.addAll(activeTasks)
        tasks.addAll(completedTasks)
        if (reverse) tasks.sortByDescending { it.id }
        return tasks
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
