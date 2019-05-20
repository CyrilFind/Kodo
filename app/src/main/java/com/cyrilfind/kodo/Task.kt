package com.cyrilfind.kodo

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Task(
    val id: String? = null,
    @Json(name = "content")
    var text: String = "",
    @Json(name = "completed")
    var checked: Boolean = false
)