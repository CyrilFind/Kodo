package com.cyrilfind.kodo

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface TodoService {
    @GET("tasks")
    suspend fun getTasks(): List<Task>?

    @POST("tasks")
    @Headers("Content-Type: application/json")
    suspend fun createTask(@Body task: Task): Task?

    @DELETE("tasks/{id}")
    suspend fun deleteTask(@Path("id") id: String): Response<ResponseBody>

    @POST("tasks/{id}/close")
    suspend fun checkTask(@Path("id") id: String): Response<ResponseBody>

    @POST("tasks/{id}/reopen")
    suspend fun uncheckTask(@Path("id") id: String): Response<ResponseBody>
}