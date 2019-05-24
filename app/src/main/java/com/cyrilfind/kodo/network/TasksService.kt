package com.cyrilfind.kodo.network

import com.cyrilfind.kodo.model.Task
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface TasksService {
    @GET("tasks")
    suspend fun getTasks(): Response<List<Task>>

    @FormUrlEncoded
    @POST("https://todoist.com/API/v8.1/items/get_completed")
    suspend fun getCompletedTasks(
        @Field("offset") offset: Int = 0,
        @Field("project_id") projectId: Long = ***REMOVED*** // default project_id
    ): Response<List<Task>>

    @POST("tasks")
    @Headers("Content-Type: application/json")
    suspend fun createTask(@Body task: Task): Response<Task>

    @DELETE("tasks/{id}")
    suspend fun deleteTask(@Path("id") id: String): Response<ResponseBody>

    @POST("tasks/{id}/close")
    suspend fun checkTask(@Path("id") id: String): Response<ResponseBody>

    @POST("tasks/{id}/reopen")
    suspend fun uncheckTask(@Path("id") id: String): Response<ResponseBody>
}