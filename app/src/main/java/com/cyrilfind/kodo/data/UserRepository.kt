package com.cyrilfind.kodo.data

import com.cyrilfind.kodo.model.UserInfo
import com.cyrilfind.kodo.network.TasksApi
import com.cyrilfind.kodo.network.TokenResponse
import com.cyrilfind.kodo.network.UserService

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class UserRepository {
    private val userService: UserService = TasksApi.INSTANCE.userService
    var user: UserInfo? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    fun logout() {
        user = null
    }

    suspend fun login(username: String, password: String): TokenResponse? {
        val response = userService.login(username, password)
        return if (response.isSuccessful) response.body() else null
    }
}
