package com.cyrilfind.kodo.network

import android.content.Context
import android.preference.PreferenceManager
import com.cyrilfind.kodo.Constants.TOKEN_PREF_KEY
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class TasksApi(private val context: Context) {

    companion object {
        lateinit var INSTANCE: TasksApi
        private const val BASE_URL = "https://android-tasks-api.herokuapp.com/api/"
    }

    val tasksService: TasksService by lazy { retrofit.create(TasksService::class.java) }
    val userService: UserService by lazy { retrofit.create(UserService::class.java) }

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addNetworkInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer ${getToken()}")
                    .build()
                chain.proceed(newRequest)
            }
            .build()
    }

    private val jsonSerializer = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private val converterFactory =
        jsonSerializer.asConverterFactory("application/json".toMediaType())

    private val retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        .addConverterFactory(converterFactory)
        .build()

    private fun getToken(): String? =
        PreferenceManager.getDefaultSharedPreferences(context).getString(
            TOKEN_PREF_KEY, "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjo1OCwiZXhwIjoxNjA4MjExODUxfQ.6DLEzX9saIuNI0hgGoQDbzRzGxhgsn5wXpfy4MD7EV8"
        )
}

