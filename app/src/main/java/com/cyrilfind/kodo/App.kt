package com.cyrilfind.kodo

import android.app.Application
import com.cyrilfind.kodo.network.TasksApi

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        TasksApi.INSTANCE = TasksApi(this)
    }
}