package com.cyrilfind.kodo.network

import com.cyrilfind.kodo.model.UserInfo
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface UserService {
    @GET("users/info")
    suspend fun getInfo(): Response<UserInfo>

    @Multipart
    @PATCH("users/update_avatar")
    suspend fun updateAvatar(@Part avatar: MultipartBody.Part): Response<UserInfo>

    @PATCH("users")
    suspend fun update(@Body user: UserInfo): Response<UserInfo>

    @POST("users/login")
    suspend fun login(@Field("username") username: String, @Field("password") password: String): Response<TokenResponse>

    @POST("users/sign_up")
    suspend fun signUp(
        @Field("firstname") firstname: String,
        @Field("lastname") lastname: String,
        @Field("email") username: String,
        @Field("password") password: String,
        @Field("confirm_password") confirmPassword: String
    ): Response<TokenResponse>
}


