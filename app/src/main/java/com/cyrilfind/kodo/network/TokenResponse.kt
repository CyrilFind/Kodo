package com.cyrilfind.kodo.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    @SerialName("token")
    val token: String
)