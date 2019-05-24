package com.cyrilfind.kodo.tasklist

import com.cyrilfind.kodo.model.Task

interface TaskNavigator {
    fun goToTaskDetail(task: Task)
}
