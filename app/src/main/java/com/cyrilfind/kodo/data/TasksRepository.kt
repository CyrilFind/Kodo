package com.cyrilfind.kodo.data

import com.cyrilfind.kodo.model.Task
import com.cyrilfind.kodo.network.TasksApi

class TasksRepository {
    private val todoService = TasksApi.INSTANCE.tasksService

    suspend fun getTasks(reverse: Boolean = false, completed: Boolean = false): List<Task>? {
        val response = todoService.getTasks()
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun createTask(task: Task): Task? {
        val createTaskResponse = todoService.createTask(task)
        return if (createTaskResponse.isSuccessful) createTaskResponse.body() else null
    }

    suspend fun updateTask(task: Task): Task? {
        val createTaskResponse = todoService.updateTask(task)
        return if (createTaskResponse.isSuccessful) createTaskResponse.body() else null
    }

    suspend fun deleteTask(task: Task): Boolean {
        return task.id != null && todoService.deleteTask(task.id).isSuccessful
    }
}
